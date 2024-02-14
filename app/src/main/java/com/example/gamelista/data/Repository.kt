package com.example.gamelista.data
import android.content.Context
import com.api.igdb.apicalypse.APICalypse
import com.api.igdb.apicalypse.Sort
import com.api.igdb.exceptions.RequestException
import com.api.igdb.request.IGDBWrapper
import com.api.igdb.request.games
import com.api.igdb.request.search
import com.api.igdb.utils.ImageSize
import com.api.igdb.utils.ImageType
import com.api.igdb.utils.imageBuilder
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameStatus
//import com.example.gamelista.model.RetrofitServiceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import proto.Search

object Repository {

//    private val service = RetrofitServiceFactory.makeRetrofitService()

    private var cache: List<Game> = listOf()
    private var lastQuery: String = ""
    private var modelListedGameList: MutableList<Game> = mutableListOf()
    private var gamesList: MutableList<Game> =
        mutableListOf() //La cambié de val a var, no sé si técnicamente está mal
    private var currentPage = 1
    private val pageSize = 20
    private var searchPage = 1
    private const val LIMIT = 20
    private var shouldRequestNewPage: Boolean = true
    private lateinit var sharedPreferencesManager: GameSharedPreferencesManager
    private const val BASE_URL = "https://api.igdb.com/v4/"
    private const val CLIENT_ID = "9ka22ciij2034pew1ovudpz2esookx"
    private const val AUTHORIZATION_TOKEN = "6tncw4nq67y7oc4ep2qgkpe3q83j5g"

    fun initialize(context: Context) {
        sharedPreferencesManager = GameSharedPreferencesManager(context)
    }
    /*
suspend fun getGames(): List<Game> = withContext(Dispatchers.IO) {
if (cache.isNotEmpty() && !shouldRequestNewPage) {
return@withContext mergeWithLocalList(cache)
}
try {
val response = service.listApiGames(currentPage)
val apiGames = response.results

createGamesFromApi(apiGames).also {
cache = cache + mergeWithLocalList(it)
}
shouldRequestNewPage = false
return@withContext cache
} catch (e: Exception) {
shouldRequestNewPage = false
return@withContext listOf()
}
}
*/
    suspend fun getGames(): List<Game> {
        return withContext(Dispatchers.IO) {
            if (cache.isNotEmpty() && !shouldRequestNewPage) {
                return@withContext mergeWithLocalList(cache)
            }
            IGDBWrapper.setCredentials(CLIENT_ID, AUTHORIZATION_TOKEN)
            val offset = (currentPage - 1) * pageSize // Calcular el offset
            val apicalypse = APICalypse()
                .fields("*,platforms.*, involved_companies.company.*, cover.*")
                .limit(pageSize)
                .offset(offset)
                .sort("rating_count", Sort.DESCENDING)
            try {
                val wrapperGames: List<proto.Game> = IGDBWrapper.games(apicalypse)
                createGamesFromWrapper(wrapperGames).also {
                    cache = cache + mergeWithLocalList(it)
                }
                shouldRequestNewPage = false
                currentPage++ // Incrementar el número de página para la siguiente solicitud
                return@withContext cache
            } catch (e: RequestException) {
                shouldRequestNewPage = false
                return@withContext listOf()
            }
        }
    }
    private fun createGamesFromWrapper(apiGames: List<proto.Game>) : List<Game> {
        return apiGames
            .filter {
                it.id.toInt() != 0 && it.name.isNotEmpty() && !it.platformsList.isNullOrEmpty()
            }
            .map { game ->
                val devsNames = game.involvedCompaniesList.mapNotNull {
                    it.company.name
                }.firstOrNull()
                if (devsNames != null) {
                    Game(
                        id = game.id,
                        titulo = game.name,
                        imagen = imageBuilder(game.cover.imageId),
                        plataforma = game.platformsList?.filter { platform ->
                            platform.name.isNotEmpty()
                        }?.joinToString(separator = ", ") { it.name }.orEmpty(),
                        status = GameStatus.SIN_CLASIFICAR,
                        fav = false,
                        sinopsis = game.id.toString(),
                        dev = devsNames
                    )
                } else {
                    Game(
                        id = game.id,
                        titulo = game.name,
                        imagen = imageBuilder(game.cover.imageId),
                        plataforma = game.platformsList?.filter { platform ->
                            platform.name.isNotEmpty()
                        }?.joinToString(separator = ", ") { it.name }.orEmpty(),
                        status = GameStatus.SIN_CLASIFICAR,
                        fav = false,
                        sinopsis = game.id.toString(),
                        dev = "Vacío"
                    )
                }
            }
    }

    private fun mergeWithLocalList(remoteGames: List<Game>): List<Game> {
        val newGames = remoteGames.map { game ->
            val found = gamesList.firstOrNull { localGame -> localGame.id == game.id }
            if (found != null) {
                found
            } else {
                game
            }
        }
        return newGames
    }

    suspend fun searchGames(query: String): List<Game> = withContext(Dispatchers.IO) {
        if (query == lastQuery && cache.isNotEmpty() && !shouldRequestNewPage) {
            return@withContext mergeWithLocalList(cache)
        }
        if (query != lastQuery) {
            searchPage = 1
        }
        val offset = (searchPage - 1) * pageSize // Calcular el offset
        IGDBWrapper.setCredentials(CLIENT_ID, AUTHORIZATION_TOKEN)
        val apicalypse = APICalypse()
            .fields("*,platforms.*, involved_companies.company.*, cover.*")
            .limit(pageSize)
            .offset(offset)
            .search(query)
//            .sort("rating_count", Sort.DESCENDING)
        try {
            val wrapperGames: List<proto.Game> = IGDBWrapper.games(apicalypse)
            createGamesFromWrapper(wrapperGames).also {
                cache = (cache + mergeWithLocalList(it)).filter { game ->
                    game.titulo.lowercase().contains(query.lowercase())
                }
                lastQuery = query
                shouldRequestNewPage = false
                searchPage++ // Incrementar el número de página para la siguiente solicitud de búsqueda
            }
            cache
        } catch (e: Exception) {
            e.printStackTrace()
            shouldRequestNewPage = false
            return@withContext listOf()
        }
    }
    fun imageBuilder(imageID: String): String {
        return imageBuilder(imageID, ImageSize.COVER_BIG, ImageType.PNG)
    }

    fun onListedItem(game: Game, status: GameStatus): List<Game> {
        if (status != GameStatus.SIN_CLASIFICAR) {
            addOrUpdateGame(game, status)
        } else {
            deleteGame(game, status)
        }
        cache = cache.map {
            if (game.id == it.id) {
                it.also { matchedGame ->
                    matchedGame.setStatusGame(status)
                }
            } else it
        }

        return cache
    }

    fun getFavoriteGames(): List<Game> {
        val savedGameList = sharedPreferencesManager.getGameList()
        // Actualiza la lista de juegos con los datos guardados
        gamesList = mergeWithLocalList(savedGameList).toMutableList()
        modelListedGameList = gamesList
        return modelListedGameList // puedes devolver gameList también, pero modelListedGameList es la que solo tiene favs y con estado definido
            .filter {
                it.fav //it.fav == true
            }
    }

    fun filterFavoriteGames(userFilter: String): List<Game> {
        val filteredGames = gamesList
            .filter { game ->
                game.fav && game.titulo.lowercase().contains(userFilter.lowercase())
            }
        return filteredGames
    }

    fun getListedGames(excludedStatus: GameStatus): List<Game> {
        val listedGameList = sharedPreferencesManager.getGameList()
        // Actualiza la lista de juegos con los datos guardados
        gamesList = mergeWithLocalList(listedGameList).toMutableList()
        modelListedGameList = gamesList
//        return gamesList
        return modelListedGameList
            .filter {
                it.status != excludedStatus
            }
    }

    private fun addOrUpdateGame(game: Game, status: GameStatus) {
        game.setStatusGame(status)

        val currentGame = modelListedGameList
            //.firstOrNull { gameInList -> gameInList.titulo == game.titulo }
            .firstOrNull { gameInList -> gameInList.id == game.id }
            ?.let { nonNullGame ->
                val index = modelListedGameList.indexOf(nonNullGame)
                modelListedGameList.set(index, game)
            }

        if (currentGame == null) {
            addGameToList(game)
        }
        saveGamesListToSharedPreferences(gamesList)
        modelListedGameList = gamesList
    }

    fun onFavItem(game: Game) {
        if (!game.fav) {
            addItemToFavorites(game)
        } else {
            removeItemFromFavorites(game)
        }
    }

    private fun addItemToFavorites(newGame: Game) {
        val actual = gamesList.firstOrNull { it.id == newGame.id }
        if (actual != null) {
            gamesList.remove(actual)
            gamesList.add(newGame.copy(fav = true))
        } else {
            gamesList.add(newGame.copy(fav = true))
        }
        saveGamesListToSharedPreferences(gamesList)
        modelListedGameList = gamesList
    }

    private fun removeItemFromFavorites(newGame: Game) {
        val actual = gamesList.firstOrNull { it.id == newGame.id }
//        val actual = modelListedGameList.firstOrNull { it.id == newGame.id }
        if (actual != null) {
            gamesList.remove(actual)
            gamesList.add(newGame.copy(fav = false))
        }
        saveGamesListToSharedPreferences(gamesList)
        modelListedGameList = gamesList
    }

    private fun addGameToList(newGame: Game) {
        val actual = gamesList.firstOrNull { it.id == newGame.id }
//        val actual = modelListedGameList.firstOrNull { it.id == newGame.id }
        if (actual != null) {
            gamesList.remove(actual)
            gamesList.add(newGame)
        } else {
            gamesList.add(newGame)
        }
        modelListedGameList = gamesList
    }

    private fun deleteGame(game: Game, status: GameStatus) {
        game.setStatusGame(status)
        saveGamesListToSharedPreferences(gamesList)
        modelListedGameList = gamesList
    }

    private fun saveGamesListToSharedPreferences(gameList: List<Game>) {
        sharedPreferencesManager.saveGameList(gameList)
    }

    fun pasarPagina() {
        currentPage++
        shouldRequestNewPage = true
    }

//    private fun createGamesFromApi(apiGames: List<com.example.gamelista.data.model.NetworkGame>): List<Game> {
//        return apiGames
//            .filter {
//                it.id != 0 && it.name.isNotEmpty() && !it.platforms.isNullOrEmpty()
//            }
//            .map { game ->
//                Game(
//                    id = game.id.toLong(),
//                    titulo = game.name,
//                    imagen = game.backgroundImage,
//                    plataforma = game.platforms?.filter { platform ->
//                        !platform.platform.name.isNullOrEmpty()
//                    }?.joinToString(separator = ", ") { it.platform.name!! }.orEmpty(),
//                    status = GameStatus.SIN_CLASIFICAR,
//                    fav = false,
//                    sinopsis = "",
//                    dev = ""
//                )
//            }
//    }


}
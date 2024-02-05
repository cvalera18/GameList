package com.example.gamelista.data

import com.example.gamelista.model.Game
import com.example.gamelista.model.GameStatus
import com.example.gamelista.model.RetrofitServiceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {

    private val service = RetrofitServiceFactory.makeRetrofitService()

    private var cache: List<Game> = listOf()
    private var lastQuery: String = ""
    private var modelListedGameList: MutableList<Game> = mutableListOf()
    private val gamesList: MutableList<Game> = mutableListOf()
    var currentPage = 1
    private var shouldRequestNewPage: Boolean = true

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
        if (query == lastQuery && cache.isNotEmpty() && !shouldRequestNewPage) return@withContext cache
        try {
            val response = service.searchGames(query, currentPage)
            val apiGames = response.results

            createGamesFromApi(apiGames).also {
                cache = (cache + mergeWithLocalList(it)).filter { game ->
                    game.titulo.lowercase().contains(query.lowercase())
                    }
                lastQuery = query
                shouldRequestNewPage = false
            }
            cache
        } catch (e: Exception) {
            e.printStackTrace()
            shouldRequestNewPage = false
            return@withContext listOf()
        }
//        getGames()
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
        modelListedGameList = gamesList
    }

    fun onFavItem(game: Game) {
        if (!game.fav) {
            addItemToFavorites(game)
        }
        else {
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
        modelListedGameList = gamesList
    }
    private fun removeItemFromFavorites(newGame: Game) {
        val actual = gamesList.firstOrNull { it.id == newGame.id }
//        val actual = modelListedGameList.firstOrNull { it.id == newGame.id }
        if (actual != null) {
            gamesList.remove(actual)
            gamesList.add(newGame.copy(fav = false))
        }
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

    private fun deleteGame(game: Game, status: GameStatus){
        game.setStatusGame(status)
        modelListedGameList = gamesList
    }

    fun pasarPagina(){
        currentPage++
        shouldRequestNewPage = true
    }

    private fun createGamesFromApi(apiGames: List<com.example.gamelista.data.model.Game>): List<Game> {
        return apiGames
            .filter {
                it.id != 0 && it.name.isNotEmpty() && !it.platforms.isNullOrEmpty()
            }
            .map { game ->
            Game(
                id = game.id,
                titulo = game.name,
                imagen = game.backgroundImage,
                plataforma = game.platforms?.filter {
                        platform -> !platform.platform.name.isNullOrEmpty()
                }?.joinToString(separator = ", ") { it.platform.name!! }.orEmpty(),
                status = GameStatus.SIN_CLASIFICAR,
                fav = false,
                sinopsis = "",
                dev = ""
            )
        }
    }

}
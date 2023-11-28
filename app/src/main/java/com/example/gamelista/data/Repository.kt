package com.example.gamelista.data

import com.example.gamelista.model.FavGameProvider
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameStatus
import com.example.gamelista.model.RetrofitServiceFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object Repository {

    private val service = RetrofitServiceFactory.makeRetrofitService()

    private var cache: List<Game> = listOf()
    private var lastQuery: String = ""
    private val modelListedGameList: MutableList<Game> = mutableListOf()


    suspend fun getGames(): List<Game> = withContext(Dispatchers.IO) {
        if (cache.isNotEmpty()) return@withContext cache
        try {
            val response = service.listApiGames()
            val apiGames = response.results

            createGamesFromApi(apiGames).also {
                cache = it
            }
        } catch (e: Exception) {
            return@withContext listOf()
        }
    }

    suspend fun searchGames(query: String): List<Game> = withContext(Dispatchers.IO) {
        if (query == lastQuery && cache.isNotEmpty()) return@withContext cache
        try {
            val response = service.searchGames(query)
            val apiGames = response.results

            createGamesFromApi(apiGames).also {
                cache = it
                lastQuery = query
            }
        } catch (e: Exception) {
            return@withContext listOf()
        }
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
                    matchedGame.setStatusGame(status.value)
                }
            } else it
        }

        return cache
    }

    private fun addOrUpdateGame(game: Game, status: GameStatus) {
        game.setStatusGame(status.value)

        val currentGame = modelListedGameList
            .firstOrNull { gameInList -> gameInList.titulo == game.titulo }
            ?.let { nonNullGame ->
                val index = modelListedGameList.indexOf(nonNullGame)
                modelListedGameList.set(index, game)
            }

        if (currentGame == null) {
            modelListedGameList.add(game)
        }
    }

    fun onFavItem(game: Game) {
        if (!game.fav) {
            game.fav = true
            FavGameProvider.modelFavGameList.add(game)
        }
        else {
            FavGameProvider.modelFavGameList.remove(game)
            game.fav = false
        }
//        cache = cache.map {
//            if (game.id == it.id) {
//                it.also { matchedGame ->
//                    matchedGame.fav = game.fav
//                }
//            } else it
//        }
    }

    private fun deleteGame(game: Game, status: GameStatus){
        game.setStatusGame(status.value)
        modelListedGameList.remove(game)
    }

    private fun createGamesFromApi(apiGames: List<com.example.gamelista.data.model.Game>): List<Game> {
        return apiGames.map { game ->
            Game(
                id=game.id,
                titulo = game.name,
                imagen = game.backgroundImage,
                plataforma = game.platforms.joinToString(separator = ", ") { it.platform.name },
                status = "Sin Clasificar",
                fav = false,
                sinopsis = "",
                dev = ""
            )
        }
    }
}
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
    private val modelListedGameList: MutableList<Game> = mutableListOf()
    private val gamesList: MutableList<Game> = mutableListOf()

    suspend fun getGames(): List<Game> = withContext(Dispatchers.IO) {
        if (cache.isNotEmpty()) return@withContext mergeWithLocalList(cache)
        try {
            val response = service.listApiGames()
            val apiGames = response.results

            createGamesFromApi(apiGames).also {
                cache = mergeWithLocalList(it)
            }
            return@withContext cache
        } catch (e: Exception) {
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
        if (query == lastQuery && cache.isNotEmpty()) return@withContext cache
        try {
            val response = service.searchGames(query)
            val apiGames = response.results

            createGamesFromApi(apiGames).also {
                cache = mergeWithLocalList(it)
                lastQuery = query
            }
        } catch (e: Exception) {
            return@withContext listOf()
        }
        getGames()
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
        return gamesList
            .filter {
                it.fav == true
            }
    }

    fun getListedGames(excludedStatus: GameStatus): List<Game> {
        return gamesList
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
            modelListedGameList.add(game)
            addGameToList(game)
        }
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
    }
    private fun removeItemFromFavorites(newGame: Game) {
        val actual = gamesList.firstOrNull { it.id == newGame.id }
        if (actual != null) {
            gamesList.remove(actual)
            gamesList.add(newGame.copy(fav = false))
        }
    }

    private fun addGameToList(newGame: Game) {
        val actual = gamesList.firstOrNull { it.id == newGame.id }
        if (actual != null) {
            gamesList.remove(actual)
            gamesList.add(newGame)
        } else {
            gamesList.add(newGame)
        }
    }

    private fun deleteGame(game: Game, status: GameStatus){
        game.setStatusGame(status)
        modelListedGameList.remove(game)
    }

    private fun createGamesFromApi(apiGames: List<com.example.gamelista.data.model.Game>): List<Game> {
        return apiGames.map { game ->
            Game(
                id = game.id,
                titulo = game.name,
                imagen = game.backgroundImage,
                plataforma = game.platforms.joinToString(separator = ", ") { it.platform.name },
                status = GameStatus.SIN_CLASIFICAR,
                fav = false,
                sinopsis = "",
                dev = ""
            )
        }
    }

}
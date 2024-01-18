package com.example.gamelista.model

class MyListProvider {
    companion object{
        val modelListedGameList: MutableList<Game> = mutableListOf()

        fun addOrUpdateGame(game: Game, status: GameStatus) {
            game.setStatusGame(status)

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

        fun deleteGame(game: Game, status: GameStatus){
                game.setStatusGame(status)
                modelListedGameList.remove(game)
        }
    }
}
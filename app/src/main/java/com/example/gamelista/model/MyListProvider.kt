package com.example.gamelista.model

class MyListProvider {
    companion object{
        val myListGameList: MutableList<Game> = mutableListOf()

        fun addOrUpdateGame(game: Game, status: GameStatus) {
            game.setStatusGame(status.value)

            val currentGame = myListGameList
                .firstOrNull { gameInList -> gameInList.titulo == game.titulo }
                ?.let { nonNullGame ->
                    val index = myListGameList.indexOf(nonNullGame)
                    myListGameList.set(index, game)
                }

            if (currentGame == null) {
                myListGameList.add(game)
            }
        }

        fun deleteGame(game: Game, status: GameStatus){
                game.setStatusGame(status.value)
                myListGameList.remove(game)
        }
    }
}
package com.example.gamelista

class MyListProvider {
    companion object{
        val myListGameList = mutableListOf<Game>()

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
    }
}
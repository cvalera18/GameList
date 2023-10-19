package com.example.gamelista.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameProvider
import com.example.gamelista.model.GameStatus
import com.example.gamelista.model.FavGameProvider
import com.example.gamelista.model.MyListProvider

class ListViewModel : ViewModel() {

    private val _gameList = MutableLiveData<List<Game>>(emptyList())
    val gameList: LiveData<List<Game>> = _gameList

    fun getListGames() {
        _gameList.value = GameProvider.modelGameList
    }

    fun onFavItem(game: Game) {

        val currentGame = GameProvider.modelGameList.first { it.titulo == game.titulo }

        if (!game.fav) {
            currentGame.fav = true
            FavGameProvider.modelFavGameList.add(game)
        } else {
            FavGameProvider.modelFavGameList.remove(game)
            currentGame.fav = false
        }

        _gameList.value = GameProvider.modelGameList
    }

    fun onListedItem(game: Game, status: GameStatus) {
        if (status != GameStatus.SIN_CLASIFICAR) {
            MyListProvider.addOrUpdateGame(game, status)
        } else {
            MyListProvider.deleteGame(game, status)
        }
        _gameList.value = GameProvider.modelGameList
    }

    fun configFilter(userFilter: String) {
            val gameFiltered =
                GameProvider.modelGameList.filter { game ->
                    game.titulo.lowercase().contains(userFilter.lowercase())
                }
            _gameList.value = gameFiltered
    }

}

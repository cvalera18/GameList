package com.example.gamelista.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamelista.model.Game
import com.example.gamelista.model.FavGameProvider
import com.example.gamelista.model.GameProvider
import com.example.gamelista.model.GameStatus
import com.example.gamelista.model.MyListProvider

class FavViewModel: ViewModel() {

    private val _favGameList = MutableLiveData<List<Game>>(emptyList())
    val favGameList: LiveData<List<Game>> = _favGameList

    fun getListGames() {
        _favGameList.value = FavGameProvider.modelFavGameList
    }

    fun configFilter(userFilter: String) {
        val gameFiltered =
            FavGameProvider.modelFavGameList.filter { game ->
                game.titulo.lowercase().contains(userFilter.lowercase())
            }
        _favGameList.value = gameFiltered
    }

    fun onFavItem(game: Game) {

        val currentGame = FavGameProvider.modelFavGameList.first { it.titulo == game.titulo }

            currentGame.fav = true
            FavGameProvider.modelFavGameList.add(game)

        _favGameList.value = FavGameProvider.modelFavGameList
    }

    fun unFavItem(game: Game) {

        val currentGame = FavGameProvider.modelFavGameList.first { it.titulo == game.titulo }

            FavGameProvider.modelFavGameList.remove(game)
            currentGame.fav = false

        _favGameList.value = FavGameProvider.modelFavGameList
    }

    fun onListedItem(game: Game, status: GameStatus) {
        if (status != GameStatus.SIN_CLASIFICAR) {
            MyListProvider.addOrUpdateGame(game, status)
        } else {
            MyListProvider.deleteGame(game, status)
        }
        _favGameList.value = FavGameProvider.modelFavGameList
    }

}

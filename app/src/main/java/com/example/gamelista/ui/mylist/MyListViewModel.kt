package com.example.gamelista.ui.mylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamelista.data.Repository
import com.example.gamelista.model.FavGameProvider
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameStatus
import com.example.gamelista.model.MyListProvider

class MyListViewModel : ViewModel() {

    private val repository = Repository
    private val _listedGameList = MutableLiveData<List<Game>>(emptyList())
    val listedGameList: LiveData<List<Game>> = _listedGameList

    fun getListGames() {
        _listedGameList.value = MyListProvider.modelListedGameList
    }

    fun configFilter(userFilter: String) {
        val gameFiltered =
            MyListProvider.modelListedGameList.filter { game ->
                game.titulo.lowercase().contains(userFilter.lowercase())
            }
        _listedGameList.value = gameFiltered
    }

    fun onFavItem(game: Game) {
        val currentGame = MyListProvider.modelListedGameList.first { it.titulo == game.titulo }

        if (!game.fav) {
            currentGame.fav = true
            FavGameProvider.modelFavGameList.add(game)
        } else {
            FavGameProvider.modelFavGameList.remove(game)
            currentGame.fav = false
        }
        _listedGameList.value = MyListProvider.modelListedGameList
    }

    fun onListedItem(game: Game, status: GameStatus) {
        _listedGameList.value = repository.onListedItem(game, status)
    }

}
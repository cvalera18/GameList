package com.example.gamelista.ui.mylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamelista.data.Repository
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameStatus
import com.example.gamelista.model.MyListProvider

class MyListViewModel : ViewModel() {

    private val repository = Repository
    private val _listedGameList = MutableLiveData<List<Game>>(emptyList())
    val listedGameList: LiveData<List<Game>> = _listedGameList

    fun getListGames() {
//        _listedGameList.value = MyListProvider.modelListedGameList
        val games = repository.getListedGames(GameStatus.SIN_CLASIFICAR)
        _listedGameList.value = games
    }

    fun configFilter(userFilter: String) {
        val gameFiltered =
            MyListProvider.modelListedGameList.filter { game ->
                game.titulo.lowercase().contains(userFilter.lowercase())
            }
        _listedGameList.value = gameFiltered
    }

    fun onFavItem(game: Game) {
        /*
        val currentGame = MyListProvider.modelListedGameList.first { it.titulo == game.titulo }

        if (!game.fav) {
        // FIXME
        //            currentGame.fav = true
        FavGameProvider.modelFavGameList.add(game)
        } else {
        // FIXME
        FavGameProvider.modelFavGameList.remove(game)
        //            currentGame.fav = false
        }
        _listedGameList.value = MyListProvider.modelListedGameList
        */
        repository.onFavItem(game)
        getListGames()
    }

    fun onListedItem(game: Game, status: GameStatus) {
        _listedGameList.value = repository.onListedItem(game, status)
        getListGames()
    }

}
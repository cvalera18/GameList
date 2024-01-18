package com.example.gamelista.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamelista.data.Repository
import com.example.gamelista.model.Game
import com.example.gamelista.model.FavGameProvider
import com.example.gamelista.model.GameStatus
import com.example.gamelista.model.MyListProvider

class FavViewModel: ViewModel() {

    private val repository = Repository
    private val _favGameList = MutableLiveData<List<Game>>(emptyList())
    val favGameList: LiveData<List<Game>> = _favGameList

    fun getListGames() {
//        _favGameList.value = FavGameProvider.modelFavGameList
        val games = repository.getFavoriteGames()
        _favGameList.value = games
    }

    fun configFilter(userFilter: String) {
        val gameFiltered =
            FavGameProvider.modelFavGameList.filter { game ->
                game.titulo.lowercase().contains(userFilter.lowercase())
            }
        _favGameList.value = gameFiltered
    }

    fun onFavItem(game: Game) {
        /*
        val currentGame = FavGameProvider.modelFavGameList.first { it.titulo == game.titulo }

        // FIXME
        //            currentGame.fav = true
        FavGameProvider.modelFavGameList.add(game)

        _favGameList.value = FavGameProvider.modelFavGameList
        */
        repository.onFavItem(game)
        getListGames()
    }

    fun unFavItem(game: Game) {
        repository.onFavItem(game) //Esta función incluye agregar y eliminar
        getListGames()
    }

    fun onListedItem(game: Game, status: GameStatus) {
        /*
        if (status != GameStatus.SIN_CLASIFICAR) {
        MyListProvider.addOrUpdateGame(game, status)
        } else {
        MyListProvider.deleteGame(game, status)
        }
        _favGameList.value = FavGameProvider.modelFavGameList
        */
        _favGameList.value = repository.onListedItem(game, status)
        getListGames()
    }

}

package com.example.gamelista.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelista.data.Repository
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameStatus
import kotlinx.coroutines.launch

class FavViewModel: ViewModel() {

    private val repository = Repository
    private val _favGameList = MutableLiveData<List<Game>>(emptyList())
    val favGameList: LiveData<List<Game>> = _favGameList

    fun getListGames() {
//        _favGameList.value = FavGameProvider.modelFavGameList
        val games = repository.getFavoriteGames()
        _favGameList.value = games
    }

    fun searchInList(userFilter: String) {
        viewModelScope.launch {
            val filteredGames = repository.filterFavoriteGames(userFilter)
            _favGameList.value = filteredGames
        }
    }

    fun onFavItem(game: Game) {
        repository.onFavItem(game)
        getListGames()
    }

    fun unFavItem(game: Game) {
        repository.onFavItem(game) //Esta función incluye agregar y eliminar
        getListGames()
    }

    fun onListedItem(game: Game, status: GameStatus) {
        _favGameList.value = repository.onListedItem(game, status)
        getListGames()
    }

}

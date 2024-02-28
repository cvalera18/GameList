package com.example.gamelista.ui.mylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelista.data.Repository
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameStatus
import com.example.gamelista.model.MyListProvider
import kotlinx.coroutines.launch

class MyListViewModel : ViewModel() {

    private val repository = Repository
    private val _listedGameList = MutableLiveData<List<Game>>(emptyList())
    val listedGameList: LiveData<List<Game>> = _listedGameList

    fun getListGames() {
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
        repository.onFavItem(game)
        getListGames()
    }

    fun onListedItem(game: Game, status: GameStatus) {
        _listedGameList.value = repository.onListedItem(game, status)
        getListGames()
    }

    fun searchInList(userFilter: String) {
        viewModelScope.launch {
            val filteredGames = repository.searchFavoriteGames(userFilter)
            _listedGameList.value = filteredGames
        }
    }

    fun filterByStatus(status: GameStatus) {
        viewModelScope.launch {
            val filteredGames = repository.getGamesByStatus(status)
            _listedGameList.value = filteredGames
        }
    }
}
package com.example.gamelista.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelista.data.Repository
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameProvider
import com.example.gamelista.model.GameStatus
import com.example.gamelista.model.FavGameProvider
import com.example.gamelista.model.MyListProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch


class ListViewModel : ViewModel() {

    private val repository = Repository
    private var currentSearchQuery = ""
    private val _gameList = MutableLiveData<List<Game>>(emptyList())
    val gameList: LiveData<List<Game>> = _gameList

    fun getListGames() {
        //_gameList.value = GameProvider.modelGameList

        viewModelScope.launch {
            _gameList.value = repository.getGames()
            //_gameList.value = repository.searchGames("sonic")
        }
    }

    fun onFavItem(game: Game) {
        repository.onFavItem(game)
        getListGames()
    }

    fun onListedItem(game: Game, status: GameStatus) {
        _gameList.value = repository.onListedItem(game, status)
        getListGames()
    }

    private val searchQueryStateFlow = MutableStateFlow("")

    fun configFilter(userFilter: String) {
        viewModelScope.launch {
            // Actualiza el valor del estado del flujo cada vez que el usuario escribe algo
            searchQueryStateFlow.value = userFilter

            // Utiliza el flujo con debounce para esperar un tiempo antes de realizar la búsqueda
            searchQueryStateFlow.debounce(500) // Ajusta el tiempo de debounce según tus necesidades (300 ms en este ejemplo)
                .collectLatest { debouncedUserFilter ->
                    // Dentro de este bloque se realizará la búsqueda después de esperar el tiempo especificado
                    if (debouncedUserFilter.isNotBlank()) {
                        val gameFiltered = repository.searchGames(debouncedUserFilter).filter { game ->
                            game.titulo.lowercase().contains(debouncedUserFilter.lowercase())
                        }
                        _gameList.value = gameFiltered
                    } else {
                        // No se ha ingresado nada, puedes manejar esto según tus necesidades
                    }
                }
        }
    }

}

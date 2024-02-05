package com.example.gamelista.ui.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamelista.data.Repository
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class ListViewModel : ViewModel() {
    private val repository = Repository
    private var currentSearchQuery = ""
    private val _gameList = MutableLiveData<List<Game>>(emptyList())
    val gameList: LiveData<List<Game>> = _gameList
    private val searchQueryStateFlow = MutableStateFlow("")
    init {
        searchQueryStateFlow
            // Utiliza el flujo con debounce para esperar un tiempo antes de realizar la búsqueda
            .debounce(500) // Ajusta el tiempo de debounce según tus necesidades (500 ms en este ejemplo)
            .onEach { debouncedUserFilter ->
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
            .launchIn(viewModelScope)
    }

    fun getListGames() {
        //_gameList.value = GameProvider.modelGameList
        viewModelScope.launch {
            if (searchQueryStateFlow.value.isNotEmpty()){
                _gameList.value = repository.searchGames(searchQueryStateFlow.value)
            } else {
                _gameList.value = repository.getGames()
            }
//            val games = repository.getGames()
//            _gameList.value = _gameList.value?.plus(games) ?: games
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


    fun configFilter(userFilter: String) {
//        viewModelScope.launch {
            // Actualiza el valor del estado del flujo cada vez que el usuario escribe algo
            searchQueryStateFlow.value = userFilter

            // Utiliza el flujo con debounce para esperar un tiempo antes de realizar la búsqueda
//            searchQueryStateFlow.debounce(500) // Ajusta el tiempo de debounce según tus necesidades (500 ms en este ejemplo)
//                .collectLatest { debouncedUserFilter ->
//                    // Dentro de este bloque se realizará la búsqueda después de esperar el tiempo especificado
//                    if (debouncedUserFilter.isNotBlank()) {
//                        val gameFiltered = repository.searchGames(debouncedUserFilter).filter { game ->
//                            game.titulo.lowercase().contains(debouncedUserFilter.lowercase())
//                        }
//                        _gameList.value = gameFiltered
//                    } else {
//                        // No se ha ingresado nada, puedes manejar esto según tus necesidades
//                    }
//                }
//        }
    }

    fun pasarPagina() {
        repository.pasarPagina()
    }

}

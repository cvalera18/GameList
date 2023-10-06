package com.example.gamelista.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameProvider

class GameViewModel : ViewModel() {

    // LiveData para la lista de juegos
    private val _gameList = MutableLiveData<List<Game>>(emptyList())
    val gameList: LiveData<List<Game>> get() = _gameList

    // Inicialización del ViewModel
    init {
        // Puedes inicializar _gameList aquí con los datos iniciales o cargarlos desde GameProvider
        _gameList.value = GameProvider.modelGameList
    }


    // Función para actualizar un juego en la lista (por ejemplo, cambiar el estado)
    fun updateGame(game: Game) {
        val currentList = _gameList.value.orEmpty().toMutableList()
        val index = currentList.indexOfFirst { it.titulo == game.titulo }
        if (index != -1) {
            currentList[index] = game
            _gameList.value = currentList
        }
    }

}
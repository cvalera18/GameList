package com.example.gamelista.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavViewModel: ViewModel() {

    private val _state = MutableLiveData<Int>(0)
    val state: LiveData<Int> = _state

    fun increment() {
        _state.value = _state.value?.plus(1)
    }
}

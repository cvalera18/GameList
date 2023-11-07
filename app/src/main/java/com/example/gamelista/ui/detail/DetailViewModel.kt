package com.example.gamelista.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gamelista.model.Game

class DetailViewModel: ViewModel() {

    private val _detailGameList = MutableLiveData<List<Game>>(emptyList())
    val detailGameList: LiveData<List<Game>> = _detailGameList


}
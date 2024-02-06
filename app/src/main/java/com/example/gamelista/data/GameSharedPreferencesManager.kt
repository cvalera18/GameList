package com.example.gamelista.data

import android.content.Context
import android.content.SharedPreferences
import com.example.gamelista.data.model.GameResponse
import com.example.gamelista.model.Game
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GameSharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveGameList(listaJuegos: List<Game>) {
        val json = gson.toJson(listaJuegos)
        sharedPreferences.edit().putString("lista_juegos", json).apply()
    }

    fun getGameList(): List<Game> {
        val json = sharedPreferences.getString("lista_juegos", "")
        val type = object : TypeToken<List<Game>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }
}
package com.example.gamelista

data class Game(
    val titulo: String,
    val imagen: String,
    val plataforma: String,
    var status: String,
    var fav: Boolean,
    var sinopsis: String,
    var dev: String
    ) {
    // Getter para status
    fun getStatusGame(): String {
        return status
    }

    // Setter para status
    fun setStatusGame(newStatus: String) {
        status = newStatus
    }
}
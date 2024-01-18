package com.example.gamelista.model

data class Game(
    val id: Int,
    val titulo: String,
    val imagen: String,
    val plataforma: String,
    var status: GameStatus,
    val fav: Boolean,
    var sinopsis: String,
    var dev: String
) {
    // Getter para status
    fun getStatusGame(): GameStatus {
        return status
    }

    // Setter para status
    fun setStatusGame(newStatus: GameStatus) {
        status = newStatus
    }
}
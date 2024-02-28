package com.example.gamelista.model

data class Game(
    val id: Long,
    val titulo: String,
    val imagen: String,
    val plataforma: List<String>,
    var status: GameStatus,
    var fav: Boolean,
    var sinopsis: String,
    var dev: String?,
    val release_date: String?
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
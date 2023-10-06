package com.example.gamelista.model

enum class GameStatus(val value: String) {
    JUGANDO("Jugando"),
    COMPLETADO("Completado"),
    PENDIENTE("Pendiente"),
    SIN_CLASIFICAR("Sin Clasificar"),
    ABANDONADO("Abandonado")
}
package com.luisavillacorte.gosportapp.jugador.adapters.model.auth

import java.util.Date

data class User(

    val nombres: String,
    val telefono: String,
    val identificacion: String,
    val ficha: String,
    val programa: String,
    val finFicha: Date,
    val jornada: String,
    val correo: String,
    val contrasena: String,
    val rol: String,
    var dorsal: String,
    var isSelected: Boolean = false

) {
    override fun toString(): String {
        return "User(nombres='$nombres', ficha='$ficha', rol='$rol')"
    }
}


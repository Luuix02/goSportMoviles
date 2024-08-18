package com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User

data class EquipoRequest(

    val nombreEquipo: String,
    val nombreCapitan: String,
    val contactoUno: String,
    val contactoDos: String,
    val jornada: String,
    val puntos: Int,
    val cedula: String,
    val imgLogo: String,
    val idLogo: String,
    val estado: Boolean,
    val participantes: List<User>


)

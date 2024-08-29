package com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo

import com.google.gson.annotations.SerializedName
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User

data class CrearEquipoResponse(

    val msg: String,
    val equipo: Equipo
)

data class  Equipo(
    val nombreEquipo: String,
    val nombreCapitan: String,
    val contactoUno: String,
    val contactoDos: String,
    val jornada: String,
    val cedula: String,
    val imgLogo: String,
    val idLogo: String,
    val estado: Boolean,
    val puntos: Int,
    val participantes: List<User>,

    )
data class BuscarJugadoresResponse(
    val participantes: List<User>
)
data class UploadResponse(
    val message: String,
    val url: String,
    val public_id: String
)
data class  Participante(
    @SerializedName("_id") val id: String,
    val nombreJugador: String,
    val ficha: String,
    var dorsal: String? = "",
    var isSelected : Boolean = false
)

data class ValidarInscripcionResponse(
    val msg: String,
    val equipo: List<Equipo>
)

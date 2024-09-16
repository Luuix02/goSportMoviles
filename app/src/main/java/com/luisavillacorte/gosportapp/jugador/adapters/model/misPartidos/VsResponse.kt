package com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos

import com.google.gson.annotations.SerializedName

data class VsResponse(
    val _id: String,
    val equipo1: EquipoResponse,
    val equipo2: EquipoResponse,
    val fecha: String,
    val hora: String,

)

data class EquipoResponse(
    val informacion: TeamInfo
)

data class TeamInfo(
    val team1: EquipoDetails? = null,
    val team2: EquipoDetails? = null
)

data class EquipoDetails(
    val Equipo: Equipo,
    val idCampeonato: String
)

data class Equipo(
    val _id: String,
    val nombreEquipo: String,
    val imgLogo: String
)

data class User(
    @SerializedName("_id") val id: String,
    val nombres: String,
    val ficha: Int,
    val dorsal: Int
)



package com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros

data class Partidos(
    val _id: String,
    val equipo1: Equipo?,
    val equipo2: Equipo?,
    val idCampeonato: String?,
    val estado: Boolean?,
    val fecha: String?,
    val hora: String?,
    val idPlanillero: String?
)
data class PosicionEquipoResponse(
    val message: String, // Si tu API devuelve un mensaje
    val posiciones: List<PosicionEquipoData> // Asegúrate de que este es el campo correcto en tu respuesta
)

data class PosicionEquipoData(
    val _id: String,
    val equipo: Equipo,
    val idCampeonato: String,
    val pts: Int,
    val goles: Int,
    val amarillas: Int,
    val rojas: Int,
    val createdAt: String,
    val updatedAt: String
)

data class Equipo(
    val _id: String?,
    val nombreEquipo: String?,
    val nombreCapitan: String?,
    val contactoUno: String?,
    val contactoDos: String?,
    val jornada: String?,
    val cedula: String?,
    val imgLogo: String?,
    val idLogo: String?,
    val estado: Boolean?,
    val participantes: List<Participante>?
)

data class Participante(
    val _id: String?,
    val nombres: String?,
    val ficha: Int?,
    val dorsal: Int?
)

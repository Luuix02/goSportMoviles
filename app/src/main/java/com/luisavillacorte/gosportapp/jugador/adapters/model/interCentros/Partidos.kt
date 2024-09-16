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

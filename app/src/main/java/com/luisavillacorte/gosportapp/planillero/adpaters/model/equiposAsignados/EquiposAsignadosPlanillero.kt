package com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados

import android.content.Context
import java.io.Serializable

class Vs(
    val _id: String?,
    val equipo1: InscripcionEquipos,
    val equipo2: InscripcionEquipos,
    val IdFase: String,
    val fecha: String? = null,
    val hora: String? = null,
    val estado: Boolean? = null,
    val idPlanillero: String,
    @Transient val context: Context? = null // example of a non-serializable field
) : Serializable

class Equipo(
    val id: String,
    val nombreEquipo: String,
    val nombreCapitan: String,
    val contactoUno: String,
    val contactoDos: String,
    val jornada: String,
    val cedula: String,
    val imgLogo: String,
    val estado: Boolean,
    val participantes: List<Participantes>
) : Serializable

class Participantes(
    val _id: String,
    val nombres: String,
    val ficha: Int,
    val dorsal: Int
)

class Informacion(
    val team1: Team,
    val team2: Team
) : Serializable

class Team(
    val id: String,
    val Equipo: Equipo,
    val idCampeonato: String
) : Serializable

class InscripcionEquipos(
    val informacion: Informacion
) : Serializable

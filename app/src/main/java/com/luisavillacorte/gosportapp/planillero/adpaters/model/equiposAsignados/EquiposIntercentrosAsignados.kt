package com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados

import android.content.Context
import java.io.Serializable

class EquiposIntercentrosAsignados (
    val _id: String?,
    val equipo1: EquipoInter,
    val equipo2: EquipoInter,
    val fecha: String? = null,
    val hora: String? = null,
    val estado: Boolean? = null,
    val idPlanillero: String,
    val idCampeonato: String,
    @Transient val context: Context? = null
): Serializable
class EquipoInter(
    val id: String,
    val nombreEquipo: String,
    val nombreCapitan: String,
    val contactoUno: String,
    val contactoDos: String,
    val jornada: String,
    val cedula: String,
    val imgLogo: String,
    val estado: Boolean,
    val participantes: List<ParticipantesInter>
) : Serializable
class ParticipantesInter(
    val _id: String,
    val nombres: String,
    val ficha: Int,
    val dorsal: Int,
    val totalGoles: Int,
    val amarillas : Int
)
package com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados

import com.google.gson.annotations.SerializedName
data class DatosJugadorDestacado(
        val jugadorDestacado: List<UsuariosJugadorDestacado>,
        val Campeonato: String
)
class UsuariosJugadorDestacado (
        @SerializedName("_id") val _id: String,
        val nombres: String,
        val telefono: String,
        val ficha: String,
        val correo: String,
        val public_id: String?,
        val url_foto: String?,
        val contrasena: String? = null,
        val identificacion: String,
        val rol: String,
        @SerializedName("esCapitan") val esCapitan: Boolean,
        val jornada: String,
        val programa: String,
        val estado: Boolean,
        val finFicha: String,
        val role: String

    )
class CampeonatoGetNombre (
        val nombreCampeonato: String,
        val _id: String,

        )


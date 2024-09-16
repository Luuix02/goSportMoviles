package com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Parcelize
data class ResultadosResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("equipo1") val equipo1: EquipoDetail,
    @SerializedName("equipo2") val equipo2: EquipoDetail,
    @SerializedName("IdVs") val idVs: String,
    @SerializedName("IdFase") val idFase: String,
    @SerializedName("estadoPartido") val estadoPartido: Boolean,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
) : Parcelable
@Parcelize
data class EquipoDetail(
    @SerializedName("Equipo1") val equipo: EquipoInfo,
    @SerializedName("tarjetasAmarillas") val tarjetasAmarillas: List<Tarjeta>,
    @SerializedName("tarjetasRojas") val tarjetasRojas: List<Tarjeta>,
    @SerializedName("goles") val goles: Goles
) : Parcelable
@Parcelize
data class EquipoInfo(
    @SerializedName("_id") val id: String,
    @SerializedName("nombreEquipo") val nombreEquipo: String,
    @SerializedName("nombreCapitan") val nombreCapitan: String,
    @SerializedName("contactoUno") val contactoUno: String,
    @SerializedName("contactoDos") val contactoDos: String,
    @SerializedName("jornada") val jornada: String,
    @SerializedName("cedula") val cedula: String,
    @SerializedName("imgLogo") val imgLogo: String,
    @SerializedName("idLogo") val idLogo: String,
    @SerializedName("estado") val estado: Boolean,
    @SerializedName("participantes") val participantes: List<Participante>,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("updatedAt") val updatedAt: String
) : Parcelable
@Parcelize
data class Participante(
    @SerializedName("_id") val id: String,
    @SerializedName("nombres") val nombres: String,
    @SerializedName("ficha") val ficha: Int,
    @SerializedName("dorsal") val dorsal: Int
) : Parcelable
@Parcelize
data class Tarjeta(
    @SerializedName("_id") val id: String,
    @SerializedName("nombres") val nombres: String,
    @SerializedName("ficha") val ficha: Int,
    @SerializedName("dorsal") val dorsal: Int
) : Parcelable
@Parcelize
data class Goles(
    @SerializedName("marcador") val marcador: Int,
    @SerializedName("jugadorGoleador") val jugadorGoleador: List<Participante>
) : Parcelable
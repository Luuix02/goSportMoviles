package com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import kotlinx.parcelize.Parcelize

data class CrearEquipoResponse(

    val msg: String,
    val equipo: Equipo
)

@Parcelize
data class  Equipo(
    @SerializedName("_id") val id: String,
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

    ) : Parcelable
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
data class CampeonatoInscripcion(
    @SerializedName("_id") val id: String,
    @SerializedName("Equipo") val equipo: Equipo,
    @SerializedName("idCampeonato") val idCampeonato: String

)

data class ValidacionResponse(
    @SerializedName("msg") val mensaje: String,
    @SerializedName("data") val data: List<List<CampeonatoInscripcion>>
)

data class EquipoInscriptoResponse(
    val msg: String,
    val inscripto: Inscripto
)

data class Inscripto(
    @SerializedName("Equipo") val equipo: Equipo,
    @SerializedName("idCampeonato") val idCampeonato: String,
    @SerializedName("_id") val id: String,

    )



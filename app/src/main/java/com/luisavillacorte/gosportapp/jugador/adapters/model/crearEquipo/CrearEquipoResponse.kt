package com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos.EquipoResponse
import kotlinx.parcelize.Parcelize

data class CrearEquipoResponse(

    val msg: String,
    val equipo: Equipo
)

@Parcelize
data class  Equipo(
    @SerializedName("_id") val id: String? = null,
    val nombreEquipo: String,
    val nombreCapitan: String,
    val contactoUno: String,
    val contactoDos: String,
    val jornada: String,
    val cedula: String,
    val imgLogo: String,
    val idLogo: String,
    val estado: Boolean = false,
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
data class EquipoInscriptoRequest(
//    @SerializedName("_id") val id: String,
    val Equipo: Equipo,
    val idCampeonato: String

)

data class VerificarEquipoResponse(
    val msg: String,
    val data: List<List<EquipoInscritoInfo>>
)
data class EquipoInscritoInfo(
    val _id: String,
    val Equipo: Equipo,
    val idCampeonato: String
)


data class EquipoInscritoData(
    val Equipo: Equipo,
    val idCampeonato: String,
    val _id: String
)

data class EquipoInscriptoResponse(
    val msg: String,
    val inscripto: EquipoInscritoData
//
//    val inscripto: List<EquipoInscritoData>
)
data class Participantes(
    @SerializedName("_id") val id: String,
    val nombres: String,
    val ficha: String,
    val dorsal: Int
)
data class EquipoEstadoRequest(
    val estado: Boolean
)





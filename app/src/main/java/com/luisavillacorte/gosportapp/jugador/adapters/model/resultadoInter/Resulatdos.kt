package com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter

import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Participante

class Resulatdos(
    val _id: String,
    val equipo1: EquipoResultado,
    val equipo2: EquipoResultado,
    val idCampeonato: String,
    val idVs: String,
    val estadoPartido: Boolean
)



data class EquipoResultado(
    val equipo1: DetalleEquipo,
    val golesE1: List<Goleador>?,
    val equipo2: DetalleEquipo,
    val golesE2: List<Goleador>?,
    val amarillasE1: List<Amarilla>?,
    val amarillasE2: List<Amarilla>?,
    val rojasE1: List<Roja>?,
    val puntos: Int
)

data class EquipoDetalle(
    val equipo: EquipoInfo?,  // Cambiado para reflejar un único objeto equipo
    val goles: List<Gol>?,    // Cambiado a 'goles' para unificar el nombre
    val amarillas: List<String>?,
    val rojas: List<String>?,
    val puntos: Int
)

data class DetalleEquipo(
    val _id: String,
    val nombreEquipo: String,
    val nombreCapitan: String,
    val contactoUno: String,
    val contactoDos: String,
    val jornada: String,
    val puntos: Int,
    val cedula: String,
    val imgLogo: String,
    val idLogo: String,
    val estado: Boolean,
    val participantes: List<Participante>,
    val createdAt: String,
    val updatedAt: String
)


data class Goleador(
    val jugador: Jugador,
    val goles: String,
    val equipo: String
)

data class EquipoInfo(
    val _id: String,
    val nombreEquipo: String,
    val nombreCapitan: String,
    val imgLogo: String,
    val contactoUno: String,
    val contactoDos: String,
    val jornada: String,
    val puntos: Int,
    val cedula: String,
    val estado: Boolean,
    val participantes: List<Participante>
)

data class Gol(
    val jugador: Jugador,
    val goles: String
) {
    override fun toString(): String {
        return "${jugador.nombres}: $goles"
    }
}
data class Amarilla(val jugador: Jugador) // Define según sea necesario
data class Roja(val jugador: Jugador)

data class Jugador(
    val _id: String,
    val nombres: String
)

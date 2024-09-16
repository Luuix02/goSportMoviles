package com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados



//data class TarjetasTojas(
//    val jugadoresTojas: List<JugadorTarejtaRojas> ?= null,
//)

//data class JugadorTarejtaRojas(
//    val _id : String,
//    val nombreJugador: String,
//    val ficha: Int,
//    val dorsal: Int,
//    var rojas: Int = 0,
//)
//
//data class TarjetasAmarilla(
//    val jugadoresAmarillas: List<JugadorTarejtaAmarilla> ?= null,
//)
//data class JugadorTarejtaAmarilla(
//    val _id : String,
//    val nombreJugador: String,
//    val ficha: Int,
//    val dorsal: Int,
//    var amarillas: Int = 0,
//)
data class Resultados(
    //val _id: String,
    val equipo1: InscripcionEquipos1,
    val equipo2: InscripcionEquipos2,
    val IdVs: String,
//    val IdFase: String,
    val estadoPartido: Boolean,
)
data class InscripcionEquipos1(
    val Equipo1: EquipoR,
    var tarjetasAmarillas: List<Participante>,
    var tarjetasRojas:List<Participante>,
    val goles: Goles

)

data class EquipoR(
    val _id: String,
    val nombreEquipo: String,
    val nombreCapitan: String,
    val contactoUno: String,
    val contactoDos: String,
    val jornada: String,
    val cedula: String,
    val imgLogo: String,
    val estado: Boolean,
    val participantes: List<Participante>,

    )
data class InscripcionEquipos2(
    val Equipo2: EquipoR,
    val tarjetasAmarillas: List<Participante>,
    var tarjetasRojas:List<Participante>,
    val goles: Goles

)
data class Participante(
    val _id: String,
    val nombres: String,
    val ficha: Int,
    val dorsal: Int,
    val totalGoles: Int
)


data class Goles(
    var marcador: Int = 0,
    val jugadorGoleador: List<Participante>
)

//data class JugadorGoleador(
//    val _id: String,
//    val nombreJugador: String,
//    val ficha: Int,
//    val dorsal: Int,
//
//    )
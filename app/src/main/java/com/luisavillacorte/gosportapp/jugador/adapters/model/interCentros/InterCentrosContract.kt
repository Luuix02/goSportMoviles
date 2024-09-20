package com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros

import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscritoData

interface InterCentrosContract {

    interface View {
        fun showPartidos(partidos: List<Partidos>)
        fun showEquiposInscritos(equipos: List<EquipoInscritoData>)
        fun showTablaPosiciones(posiciones: List<PosicionEquipoData>)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadPartidos(equipoId: String)
        fun loadEquiposInscritos(idCampeonato: String)
        fun loadTablaPosiciones(idCampeonato: String)
    }
}
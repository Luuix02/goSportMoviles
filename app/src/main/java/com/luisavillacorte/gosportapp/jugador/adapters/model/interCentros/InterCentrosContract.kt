package com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros

interface InterCentrosContract {

    interface View {
        fun showPartidos(partidos: List<Partidos>)
        fun showError(message: String)
    }

    interface Presenter {
        fun loadPartidos(equipoId: String)
    }
}
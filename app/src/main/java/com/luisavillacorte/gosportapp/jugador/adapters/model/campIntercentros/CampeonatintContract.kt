package com.luisavillacorte.gosportapp.jugador.adapters.model.campIntercentros

interface CampeonatintContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun mostrarCampeonatos(campeonatos: List<CampeonatInter>)
        fun showError(message: String)
        fun showSuccess(message: String)
    }

    interface Presenter {
        fun obtenerCampeonatos()
    }
}
package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

interface HomeCampeonatosContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showCampeonatos(campeonato: List<Campeonatos>)
        fun showError(message: String)

    }

    interface Presenter {
        fun getCampeonatos()



    }
}
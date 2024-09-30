package com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter

interface ResultadosContract {

    interface View {
        fun showResultados(resultados: List<Resulatdos>)
        fun showError(message: String)
    }

    interface Presenter {
        fun getResultados(idCampeonato: String)
    }

    interface Model {
        fun fetchResultados(idCampeonato: String, onFinished: OnFinishedListener)

        interface OnFinishedListener {
            fun onSuccess(resultados: List<Resulatdos>)
            fun onFailure(t: Throwable)
        }
    }
}

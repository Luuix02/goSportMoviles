package com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos

interface MisPartidosContract {
    interface View {
        fun mostrarVs(vsList: List<VsResponse>)
        fun showError(message: String)
        fun mostrarResultados(resultados: ResultadosResponse)
    }

    interface Presenter {
        fun obtenerVsEquipo()
        fun cargarResultados(idVs: String)
    }
}
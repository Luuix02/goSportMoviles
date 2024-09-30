package com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos

interface MisPartidosContract {
    interface View {
        fun mostrarVs(vsList: List<VsResponse>)
        fun showError(message: String)
        fun mostrarResultados(resultados: ResultadosResponse)
        fun mostrarMensaje(message: String)
        fun navegarAIntercentros()
        fun navegarAMisPartidos()
    }

    interface Presenter {
        fun obtenerVsEquipo()
        fun cargarResultados(idVs: String)
        fun verificarTipoCampeonatoYRedirigir()

    }
}
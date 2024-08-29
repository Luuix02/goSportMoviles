package com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados

interface ResultadosContract {
    interface View{
        fun onResultadosRecibidos(resultados: Resultados)
        fun error(error: String)
    }
    interface Presenter{
        fun obtenerResultados(idVs: String)
    }
}
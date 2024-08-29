package com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados

import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Resultados

interface verResultadosContract {
    interface View{
        fun onVerReultados(resultados: Resultados)
        fun error(error: String)
    }
    interface Presenter{
        fun obtenerVerResultados(idVs: String)
    }
}
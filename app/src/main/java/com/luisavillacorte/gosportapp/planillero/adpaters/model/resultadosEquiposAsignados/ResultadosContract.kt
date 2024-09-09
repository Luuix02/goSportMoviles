package com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados

import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs

interface ResultadosContract {
        interface View{
            //fun onResultadosRecibidos(resultados: Resultados)
            fun onResultadosRecibidos(vs: Vs)
            fun error(error: String)
            fun messageExito (message:String)
        }
        interface Presenter{
            fun obtenerResultados(idVs: String)
            fun subirDatosResultados (resultados: Resultados)
        }
    }
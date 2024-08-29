package com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados

interface EquiposAsignadosContract {
    interface View{
        fun onEquposRecibidos(equiposAsignados: List<Vs>)
        fun error(error: String)
    }
    interface Presenter{
        fun obtenerEquiposAsignados(idFase: String)
    }
}
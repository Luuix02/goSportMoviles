package com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse

interface EquiposAsignadosContract {
    interface View{
        fun onEquposRecibidos(equiposAsignados: List<Vs>)
        fun PerfilPlanillero(perfil: PerfilUsuarioResponse)
        fun error(error: String)


    }
    interface Presenter{
        fun obtenerEquiposAsignados(idPlanillero: String);
        fun obtenerPerfilUsuario()
    }
}
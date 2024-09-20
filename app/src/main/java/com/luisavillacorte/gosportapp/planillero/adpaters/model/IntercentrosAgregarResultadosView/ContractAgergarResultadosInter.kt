package com.luisavillacorte.gosportapp.planillero.adpaters.model.IntercentrosAgregarResultadosView

import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.EquiposIntercentrosAsignados

interface ContractAgergarResultadosInter {
    interface View{
        fun onIntercentrosRecibidos(intercentros: EquiposIntercentrosAsignados)
        fun error(error: String)
    }
    interface Presenter{
        fun obtenerDatosIntercentros(idVs: String)

    }
}
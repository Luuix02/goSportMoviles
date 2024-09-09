package com.luisavillacorte.gosportapp.planillero.adpaters.model.EditarCuentaPlanillero

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse

interface ContractEditarCuentaPlanillero {
    interface View{




        fun PerfilPlanillero(perfil:PerfilUsuarioResponse)
        //fun SaveDatos(public_Id: String, fotoPath: String)
        fun showError(message: String)
        fun error(error: String)
        fun showSuccess(message: String)


    }
    interface Presenter{
        fun obtenerPerfilUsuario()
        fun updatePerfilPlanillero(id: String, actualizar: DatosPlanilleroActualizar)

        fun subirFoto( id: String,fotoPath: String)
        fun actualizarFoto( id: String,fotoPath: String, public_Id: String)
        fun eliminarFoto(id: String, public_Id: String)
    }
}
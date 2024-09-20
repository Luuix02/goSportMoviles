package com.luisavillacorte.gosportapp.jugador.adapters.model.cambiarContrasena

interface CambiarContrasenaContract {
    interface View {
        fun showSuccess(message: String)
        fun showError(message: String)
    }
    interface Presenter{

    }
}
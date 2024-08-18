package com.luisavillacorte.gosportapp.jugador.adapters.model.login

interface LoginContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showError(message: String)
        fun showSuccess(token: String)
        fun navigateToPlanillero()
        fun navigateToJugador()
    }

    interface Presenter {
        fun loginUser(correo: String, contrasena: String)
    }
}
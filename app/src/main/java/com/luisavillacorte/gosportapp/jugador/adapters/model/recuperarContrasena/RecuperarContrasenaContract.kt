package com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena

interface RecuperarContrasenaContract {

    interface SolicitarCodigoView {
        fun navigateToVerificationScreen(correo: String)
        fun showError(message: String)
    }

    interface VerificarCodigoView {
        fun navigateToChangePasswordScreen(correo: String)
        fun showError(message: String)
    }

    interface CambiarContrasenaView {
        fun navigateToLoginScreen()
        fun showError(message: String)
    }

    interface Presenter {
        fun solicitarCodigo(correo: String)
        fun verificarCodigo(correo: String, codigo: String)
        fun cambiarContrasena(correo: String, nuevaContrasena: String)
    }

























//        fun showLoading()
//
//        fun hideLoading()
//
//        fun showSuccess(message: String)
//
//        fun showError(message: String)

//        fun showEmailInput()

//        fun showVerificationInput()

//        fun showNewPasswordInput()
//        fun navigateToVerificationScreen(correo: String)
//    }
//
//    interface Presenter {
//
//        fun solicitarCodigo(correo: String)
//
//        fun verificarCodigo(correo: String, codigo: String)
//
//        fun cambiarContrasena(correo: String, nuevaContrasena: String)
    }




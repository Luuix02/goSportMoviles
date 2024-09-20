package com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena

import com.luisavillacorte.gosportapp.jugador.adapters.apiService.authService.ApiService
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.recuperarContrasena.RecuperarContrasenaApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.AuthResponse
import retrofit2.Callback

//class RecuperarContrasenaService(
//    private val apiService: RecuperarContrasenaApiService
//) {
//    fun solicitarCodigo(correo: String, callback: Callback<SolicitarCodigoResponse>) {
//        val call = apiService.solicitarCodigo(SolicitarCodigoRequest(correo))
//        call.enqueue(callback)
//    }
//
//    fun verificarCodigo(correo: String, codigo: String, callback: Callback<VerificarCodigoResponse>) {
//        val call = apiService.validarCodigo(VerificarCodigoRequest(correo, codigo))
//        call.enqueue(callback)
//    }
//
//    fun cambiarContrasena(correo: String, nuevaContrasena: String, callback: Callback<AuthResponse.CambiarContrasenaResponse>) {
//        val call = apiService.cambiarContrasena(
//            CambiarContrasenaRequest(
//                correo,
//                nuevaContrasena
//            )
//        )
//        call.enqueue(callback)
//    }
//}
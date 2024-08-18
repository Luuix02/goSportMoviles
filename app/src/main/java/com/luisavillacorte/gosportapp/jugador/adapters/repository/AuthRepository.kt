package com.luisavillacorte.gosportapp.jugador.adapters.repository

import com.luisavillacorte.gosportapp.jugador.adapters.apiService.authService.ApiService

class AuthRepository {

    class AuthRepository(private val apiService: ApiService) {}

//    fun checkEmailExists(correo: String, callback: (Boolean) -> Unit) {
//        val request = EmailCheckRequest(correo)
//        val call = ApiService.verifyEmail(request)
//        call.enqueue(object : Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                if (response.isSuccessful) {
//                    val emailExists = response.body()?.emailExists ?: false
//                    callback(emailExists)
//                } else {
//                    callback(false)
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                callback(false)
//            }
//        })
//    }
//
//    fun checkIdentificationExists(identificacion: String, callback: (Boolean) -> Unit) {
//        val request = IdentificacionCheckRequest(identificacion = identificacion)
//        val call = ApiService.verifyIdentification(request)
//        call.enqueue(object : Callback<AuthResponse> {
//            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
//                if (response.isSuccessful) {
//                    val identificationExists = response.body()?.identificationExists ?: false
//                    callback(identificationExists)
//                } else {
//                    callback(false)
//                }
//            }
//
//            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
//                callback(false)
//            }
//        })
//    }
//}
}
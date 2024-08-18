package com.luisavillacorte.gosportapp.jugador.adapters.apiService.authService

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.AuthResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.LoginCredentials
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

@POST("auth/register")
    fun registerUser(@Body user: User): Call<AuthResponse>

    @POST("auth/login")
    fun loginUser(@Body loginRequest: LoginCredentials): Call<AuthResponse>

//    @POST("auth/register") // Usar el mismo endpoint para verificación
//    fun verifyEmail(@Body emailRequest: EmailCheckRequest): Call<AuthResponse>
//
//    @POST("auth/register") // Usar el mismo endpoint para verificación
//    fun verifyIdentification(@Body identificationRequest: IdentificacionCheckRequest): Call<AuthResponse>
}

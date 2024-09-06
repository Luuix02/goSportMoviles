package com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path


interface HomeApiService {
    @GET("campeonato")
    fun getCampeonato(): Call<List<Campeonatos>>

    @GET("/usuarios/perfil")
    fun obtenerPerfilUsuario(@Header("Authorization") token: String): Call<PerfilUsuarioResponse>

    @PATCH("/usuarios/{id}")
    fun actualizarPerfilUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body perfilActualizado: PerfilUsuarioResponse
    ): Call<PerfilUsuarioResponse>
    @PATCH("/usuarios/{id}")
    fun cambiarContrasena(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body nuevaContrasena: NuevaContrasenaRequest
    ): Call<Void>

}


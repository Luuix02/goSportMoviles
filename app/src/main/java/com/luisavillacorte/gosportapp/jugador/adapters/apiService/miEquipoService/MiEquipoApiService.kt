package com.luisavillacorte.gosportapp.jugador.adapters.apiService.miEquipoService

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface MiEquipoApiService {

    @GET("/equipoInscripto/validarJugador")
    fun validarUsuarioEnEquipo(
        @Header("idjugador")
        idJugador: String
    ): Call<ValidarInscripcionResponse>

    @GET("/usuarios/perfil")
    fun obtenerPerfilUsuario(@Header("Authorization") token: String): Call<PerfilUsuarioResponse>

}
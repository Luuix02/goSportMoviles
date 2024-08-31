package com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeApiService {
    @GET("campeonato")
    fun getCampeonato(): Call<List<Campeonatos>>


    @GET("/usuarios/perfil")
    fun obtenerPerfilUsuario(@Header("Authorization") token: String): Call<PerfilUsuarioResponse>


}

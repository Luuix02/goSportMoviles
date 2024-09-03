package com.luisavillacorte.gosportapp.jugador.adapters.apiService.perfilJugador

import com.luisavillacorte.gosportapp.jugador.adapters.model.perfil.PlayerProfile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface PlayerApiService {
    @GET("jugador")
    fun getPlayerProfile(@Header("Authorization") token: String): Call<List<PlayerProfile>>
}

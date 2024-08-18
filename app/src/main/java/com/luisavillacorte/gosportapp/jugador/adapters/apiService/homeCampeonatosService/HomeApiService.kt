package com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService

import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import retrofit2.Call
import retrofit2.http.GET

interface HomeApiService {
    @GET("campeonato")
    fun getCampeonato(): Call<List<Campeonatos>>
}
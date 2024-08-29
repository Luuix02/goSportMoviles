package com.luisavillacorte.gosportapp.planillero.adpaters.api.vsEquiposResultados

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceResultados {
    @GET("resultados/{idVs}")
    fun getResultados(@Path("idVs")idVs:String): Call<Any>
}
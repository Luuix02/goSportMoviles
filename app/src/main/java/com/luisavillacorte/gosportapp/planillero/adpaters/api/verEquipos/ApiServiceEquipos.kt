package com.luisavillacorte.gosportapp.planillero.adpaters.api.verEquipos

import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiServiceEquipos {
    @GET("vs")
    fun equiposAsignados(@Header("IdFase") IdFase:String): Call<List<Vs>>

    @GET("vs/{idVs}")
    fun detalleVs(@Path("idVs") idVs: String): Call<Vs>
}
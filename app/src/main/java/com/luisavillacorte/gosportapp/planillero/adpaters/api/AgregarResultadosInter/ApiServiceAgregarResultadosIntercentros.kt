package com.luisavillacorte.gosportapp.planillero.adpaters.api.AgregarResultadosInter

import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.EquiposIntercentrosAsignados
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceAgregarResultadosIntercentros {
    @GET("vsInter/{idVs}")
    fun detalleVsIntercentros(@Path("idVs") idVs: String): Call<EquiposIntercentrosAsignados>

}
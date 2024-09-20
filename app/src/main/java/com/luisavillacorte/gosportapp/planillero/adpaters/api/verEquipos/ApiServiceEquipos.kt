package com.luisavillacorte.gosportapp.planillero.adpaters.api.verEquipos

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.EquiposIntercentrosAsignados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiServiceEquipos {
    @GET("vs/vsAsignadosPlanillero")
    fun equiposAsignados(@Header("identificacion") idPlanillero:String): Call<List<Vs>>

    @GET("vs/{idVs}")
    fun detalleVs(@Path("idVs") idVs: String): Call<Vs>

    @GET("/usuarios/perfil")
    fun getPerfilPlanillero(@Header("Authorization") token: String): Call<PerfilUsuarioResponse>

    @GET("vsInter/vsAsignadosPlanilleroIntercentros/{id}")
    fun equiposAsignadosIntercentros(@Path("id") id:String): Call<List<EquiposIntercentrosAsignados>>


}
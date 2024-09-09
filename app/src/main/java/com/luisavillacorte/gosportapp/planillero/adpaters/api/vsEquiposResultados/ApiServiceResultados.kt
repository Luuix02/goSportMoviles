package com.luisavillacorte.gosportapp.planillero.adpaters.api.vsEquiposResultados

import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Resultados
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServiceResultados {

        @GET("resultados/{idVs}")
        fun getResultados(@Path("idVs")idVs:String): Call<Any>
        @GET("vs/{idVs}")
        fun detalleVs(@Path("idVs") idVs: String): Call<Vs>

        @POST("resultados/")
        fun subirResultados (@Body resultados: Resultados): Call<Resultados>

}

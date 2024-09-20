package com.luisavillacorte.gosportapp.planillero.adpaters.api.vsEquiposResultados

import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.CampeonatoGetNombre
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.DatosJugadorDestacado
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Resultados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.UsuariosJugadorDestacado
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

        @GET("usuarios/id/{id}")
        fun getDatosUsuario(@Path("id") id:String): Call<UsuariosJugadorDestacado>

        @GET("campeonato/{idCam}")
        fun getCampeonato(@Path("idCam") idCam:String): Call<CampeonatoGetNombre>

        @POST("jugadorDestacado")
        fun agregarJugadorDestacado (@Body jugadorDestacado: DatosJugadorDestacado): Call<DatosJugadorDestacado>

}

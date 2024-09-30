package com.luisavillacorte.gosportapp.jugador.adapters.apiService.misPartidosService


import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos.ResultadosResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos.VsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.http.Header

interface MisPartidosApiService {



    @GET("campeonato/detalle/{id}")
    fun getDetalleCampeonato(@Path("id") idCampeonato: String): Call<Campeonatos>

    @GET("/vs/buscar/{equipoId}")
    fun obtenerVsMiEquipo(
        @Path("equipoId") equipoId: String
    ): Call<List<VsResponse>>

    @GET("/resultados/{id}")
    fun obtenerResultadosMiEquipoVs(
        @Path("id") idVs: String,
    ): Call<ResultadosResponse>

}
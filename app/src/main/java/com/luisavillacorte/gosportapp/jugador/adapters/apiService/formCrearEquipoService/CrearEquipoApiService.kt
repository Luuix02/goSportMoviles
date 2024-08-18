package com.luisavillacorte.gosportapp.jugador.adapters.apiService.formCrearEquipoService

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.CrearEquipoResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CrearEquipoApiService {

    @POST("/inscripcionEquipos")
    fun crearEquipo(@Body equipo: Equipo): Call<CrearEquipoResponse>


    @GET("/usuarios/perfil")
    fun obtenerPerfilUsuario(@Header("Authorization") token: String): Call<PerfilUsuarioResponse>



    @GET("/usuarios/identificacion/buscar")
    fun buscarJugadoresPorIdent(
        @Header("Authorization") token: String,
        @Query("identificacion") identificacion: String): Call<List<User>>
}
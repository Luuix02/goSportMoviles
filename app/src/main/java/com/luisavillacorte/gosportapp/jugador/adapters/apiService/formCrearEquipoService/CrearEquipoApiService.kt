package com.luisavillacorte.gosportapp.jugador.adapters.apiService.formCrearEquipoService

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.CrearEquipoResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoEstadoRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.UploadResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface CrearEquipoApiService {

    @POST("/inscripcionEquipos")
    fun crearEquipo(@Body equipo: Equipo): Call<CrearEquipoResponse>


    @GET("/usuarios/perfil")
    fun obtenerPerfilUsuario(@Header("Authorization") token: String): Call<PerfilUsuarioResponse>


    @GET("/usuarios/identificacion/buscar")
    fun buscarJugadoresPorIdent(
        @Header("Authorization") token: String,
        @Query("identificacion") identificacion: String
    ): Call<List<User>>

    @Multipart
    @POST("/inscripcionEquipos/{userId}/logoEquipo")
    fun subirLogoEquipo(
        @Path("userId") userId: String,
        @Part file: MultipartBody.Part
    ): Call<UploadResponse>


    @GET("/equipoInscripto/validarJugador")
    fun validarInscripcionIntegrante(
        @Header("idjugador")
        idJugador: String
    ): Call<ValidarInscripcionResponse>



}

// verificar inscripcion en campeonato
//    @GET("/equipoInscripto/validarInscripcion")
//    fun  verificarInscripcionCampeonato(
//        @Query("idEquipo")
//        idEquipo: String
//    ): Call<VerificarInscripcionResponse>




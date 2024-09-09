package com.luisavillacorte.gosportapp.jugador.adapters.apiService.gestionarMiEquipoService

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.UploadResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface GestionarMiEquipoApiService {
    @PATCH("inscripcionEquipos/completo/{id}")
    fun actualizarEquipo(
        @Path("id") equipoId: String,
        @Body equipo: Equipo
    ): Call<Equipo>

    @Multipart
    @PATCH("inscripcionEquipos/{id}/{idLogo}")
    fun actualizarLogoEquipo(
        @Path("id") equipoId: String,
        @Path("idLogo") publicId: String,
        @Part file: MultipartBody.Part
    ): Call<ResponseBody>

    @GET("/usuarios/identificacion/buscar")
    fun buscarJugadoresPorIdent(
        @Header("Authorization") token: String,
        @Query("identificacion") identificacion: String
    ): Call<List<User>>

    @GET("/equipoInscripto/validarJugador")
    fun validarInscripcionIntegrante(
        @Header("idjugador")
        idJugador: String
    ): Call<ValidarInscripcionResponse>

}



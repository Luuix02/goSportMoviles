package com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.campIntercentros.CampeonatInter
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscriptoRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscriptoResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscritoData
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.VerificarEquipoResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.editarPerfil.Programas
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.Partidos
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.PosicionEquipoData
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.PosicionEquipoResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter.Resulatdos
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path


interface HomeApiService {
    @GET("campeonato")
    fun getCampeonato(): Call<List<Campeonatos>>

    @GET("campeonato")
    fun traerCampeonatosIntercentros(): Call<List<CampeonatInter>>

    @GET("/usuarios/perfil")
    fun obtenerPerfilUsuario(@Header("Authorization") token: String): Call<PerfilUsuarioResponse>


    @GET("/equipoInscripto/validarJugador")
    fun validarUsuarioEnEquipo(
        @Header("idjugador")
        idJugador: String
    ): Call<ValidarInscripcionResponse>

    @GET("/vsInter/buscarInter/{equipoId}")
    fun getPartidosJugados(@Path("equipoId") equipoId: String): Call<List<Partidos>>


    @Multipart
    @POST("/usuarios/{id}/foto")
    fun subirFotousuario(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Call<PerfilUsuarioResponse>

    @DELETE("/usuarios/{id}/eli")
    fun eliminarFotoUsuario(
        @Path("id") userId: String,
        @Header("Authorization") token: String
    ): Call<PerfilUsuarioResponse>

    @GET("/programa")
    fun getProgramas(): Call<List<Programas>>

//    @GET("/posicionesIntercentros")
//    fun getPosiciones(
//        @Header("idCampeonato") idCampeonato: String,
//        @Header("Authorization") token: String
//    ): Call<PosicionEquipoResponse>
@GET("/posicionesIntercentros")
fun getPosiciones(
    @Header("idCampeonato") idCampeonato: String,
    @Header("Authorization") token: String
): Call<List<PosicionEquipoData>>


    @PUT("/programa/{id}")
    fun actualizarPrograma(
        @Path("id") idPrograma: String,
        @Header("Authorization") token: String,
        @Body programa: Programas
    ): Call<Void>

    @Multipart
    @PATCH("/usuarios/{id}/pati")
    fun actualizarfoto(
        @Path("id") userId: String,
        @Header("Authorization") token: String,
        @Part body: MultipartBody.Part
    ): Call<PerfilUsuarioResponse>


    @PATCH("/usuarios/{id}")
    fun actualizarPerfilUsuario(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body perfilActualizado: PerfilUsuarioResponse
    ): Call<PerfilUsuarioResponse>

    @PATCH("/usuarios/{id}")
    fun cambiarContrasena(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body nuevaContrasena: NuevaContrasenaRequest
    ): Call<Void>

    @GET("equipoInscripto/validarInscripcion")
    fun verificarEquipoEnCampeonato(
        @Header("cedulajugador") identificacion: String
    ): Call<VerificarEquipoResponse>

    @POST("/equipoInscripto")
    fun inscribirEquipoCampeonato(
        @Body equipoData: EquipoInscriptoRequest
    ): Call<EquipoInscriptoResponse>

    @GET("resultadosInterCentros")
    fun getResultados(@Header("idCampeonato") idCampeonato: String): Call<List<Resulatdos>>



    @GET("/equipoInscripto")
    fun obtenerEquiposInscritos(
        @Header("id") idCampeonato: String
    ): Call<List<EquipoInscritoData>>

}


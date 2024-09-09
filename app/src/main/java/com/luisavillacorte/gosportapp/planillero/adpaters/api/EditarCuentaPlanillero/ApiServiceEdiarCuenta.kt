package com.luisavillacorte.gosportapp.planillero.adpaters.api.EditarCuentaPlanillero

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.CloudinaryResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.planillero.adpaters.model.EditarCuentaPlanillero.DatosPlanilleroActualizar
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceEdiarCuenta {
    @GET("/usuarios/perfil")
    fun getPerfilPlanillero(@Header("Authorization") token: String): Call<PerfilUsuarioResponse>

    @PATCH("usuarios/{id}")
    fun actualizarDatosPlanillero(@Path("id") id: String,
                                   @Header("Authorization") token: String,
                                   @Body datosPlanillero: DatosPlanilleroActualizar): Call<PerfilUsuarioResponse>

    @Multipart
    @POST("/usuarios/{id}/foto")
         fun subirFotoPlanillero(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): Call<PerfilUsuarioResponse>

    @DELETE("usuarios/{id}/eli")
     fun eliminarFotoPlanillero(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Query("public_id") publicId: String
    ): Call<PerfilUsuarioResponse>
@Multipart
    @PATCH("usuarios/{id}/pati")
     fun actualizarFotoPlanillero(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part

        ): Call<PerfilUsuarioResponse>
}
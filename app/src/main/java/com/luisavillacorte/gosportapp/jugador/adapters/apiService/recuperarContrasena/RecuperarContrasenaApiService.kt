package com.luisavillacorte.gosportapp.jugador.adapters.apiService.recuperarContrasena

import androidx.compose.ui.graphics.SolidColor
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.AuthResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.CambiarContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.CambiarContrasenaResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.SolicitarCodigoRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.SolicitarCodigoResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.VerificarCodigoRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.VerificarCodigoResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RecuperarContrasenaApiService {

    @POST("/auth/solicitar-codigo")
        fun solicitarCodigo(
            @Body request: SolicitarCodigoRequest
        ) : Call<SolicitarCodigoResponse>


    @POST("/auth/verificar-codigo")
        fun validarCodigo(
            @Body request: VerificarCodigoRequest
        ) : Call<VerificarCodigoResponse>

    @POST("/auth/cambio")
    fun cambiarContrasena(
        @Body request: CambiarContrasenaRequest
    ) : Call<CambiarContrasenaResponse>

}
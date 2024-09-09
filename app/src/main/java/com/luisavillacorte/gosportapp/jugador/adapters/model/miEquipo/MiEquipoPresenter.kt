package com.luisavillacorte.gosportapp.jugador.adapters.model.miEquipo

import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.miEquipoService.MiEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import retrofit2.http.Tag
import kotlin.coroutines.coroutineContext

class MiEquipoPresenter(
    private val view: MiEquipoContract.View,
    private val apiService: MiEquipoApiService,
    private val context: Context,
) : MiEquipoContract.Presenter {

    private val tokenManager = TokenManager(context)
    private val TAG = "MiEquipoPresenter"
    override fun getPerfilUsuario() {
        val token = tokenManager.getToken() ?: return view.mostrarError("Token no disponible")
        Log.d(TAG, "Token obtenido: $token")
        val call = apiService.obtenerPerfilUsuario("Bearer $token")
        call.enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(
                call: Call<PerfilUsuarioResponse>,
                response: Response<PerfilUsuarioResponse>
            ) {
                if (response.isSuccessful) {
                    val perfil = response.body()
                    if (perfil != null) {
//                       userCedula = perfil.identificacion
                        view.showPerfilUsuario(perfil)
                    } else {
                        view.mostrarError("Perfil de usuario vacío")
                    }
                } else {
                    view.mostrarError("Error al obtener el perfil ${response.code()}: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.mostrarError(t.message ?: "Error desconocido")
            }
        })
    }


    override fun validarInscripcionJugador(idJugador: String) {
        val call = apiService.validarUsuarioEnEquipo(idJugador)
        call.enqueue(object : Callback<ValidarInscripcionResponse> {
            override fun onResponse(
                call: Call<ValidarInscripcionResponse>,
                response: Response<ValidarInscripcionResponse>
            ) {
                if (response.isSuccessful) {
                    val validarInscripcionResponse = response.body()
                    if (validarInscripcionResponse != null && validarInscripcionResponse.equipo.isNotEmpty()) {
                        val equipo = validarInscripcionResponse.equipo[0]
                        view.mostrarEquipo(equipo)
                    } else {
                        view.mostrarError("No perteneces a ningún equipo.")
                    }
                } else {
                    view.mostrarError("Error en la respuesta del servidor.")
                }
            }

            override fun onFailure(call: Call<ValidarInscripcionResponse>, t: Throwable) {
                view.mostrarError("Error al conectar con el servidor: ${t.message}")
            }
        })
    }


}
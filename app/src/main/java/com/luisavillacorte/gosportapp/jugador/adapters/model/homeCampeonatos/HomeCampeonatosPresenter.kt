package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import android.content.Context
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeCampeonatosPresenter(
    private val view: HomeCampeonatosContract.View,
    private val context: Context,
    private val apiService: HomeApiService
) : HomeCampeonatosContract.Presenter {

    private val tokenManager = TokenManager(context)
    private val TAG = "HomePresenter"

    fun actualizarPerfilUsuario(perfilActualizado: PerfilUsuarioResponse) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val userId = tokenManager.getUserId() // Recupera el userId del TokenManager
        Log.d(TAG, "Token obtenido: $token")
        Log.d(TAG, "User ID en actualizarPerfilUsuario: $userId")

        userId?.let {
            val call = apiService.actualizarPerfilUsuario("Bearer $token", it, perfilActualizado)
            call.enqueue(object : Callback<PerfilUsuarioResponse> {
                override fun onResponse(
                    call: Call<PerfilUsuarioResponse>,
                    response: Response<PerfilUsuarioResponse>
                ) {
                    if (response.isSuccessful) {
                        val perfil = response.body()
                        if (perfil != null) {
                            view.traernombre(perfil)
                        } else {
                            view.showError("Perfil de usuario vacío")
                        }
                    } else {
                        view.showError("Error al actualizar el perfil ${response.code()}: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                    view.showError(t.message ?: "Error desconocido")
                }
            })
        } ?: view.showError("User ID no disponible")
    }

    fun cambiarContrasena(nuevaContrasenaRequest: NuevaContrasenaRequest) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val userId = tokenManager.getUserId() // Recupera el userId del TokenManager
        Log.d(TAG, "Token obtenido: $token")
        Log.d(TAG, "User ID en cambiarContrasena: $userId")

        userId?.let {
            val call = apiService.cambiarContrasena("Bearer $token", it, nuevaContrasenaRequest)
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        view.showSuccess("Contraseña cambiada exitosamente")
                    } else {
                        view.showError("Error al cambiar la contraseña ${response.code()}: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    view.showError(t.message ?: "Error desconocido")
                }
            })
        } ?: view.showError("User ID no disponible")
    }

    override fun getPerfilUsuario() {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
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
                        view.traernombre(perfil)
                        tokenManager.saveUserId(perfil.id) // Guarda el userId en TokenManager
                        Log.d(TAG, "User ID guardado: ${perfil.id}")
                    } else {
                        view.showError("Perfil de usuario vacío")
                    }
                } else {
                    view.showError("Error al obtener el perfil ${response.code()}: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.showError(t.message ?: "Error desconocido")
            }
        })
    }

    override fun getCampeonatos() {
        view.showLoading()
        Log.d(TAG, "Fetching campeonatos from API")

        val call = apiService.getCampeonato()
        call.enqueue(object : Callback<List<Campeonatos>> {
            override fun onResponse(
                call: Call<List<Campeonatos>>,
                response: Response<List<Campeonatos>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { campeonatos ->
                        val campeonatosFiltrados = campeonatos.filter {
                            it.estadoCampeonato == "Ejecucion"
                        }

                        view.showCampeonatos(campeonatosFiltrados)
                        Log.d(TAG, "Campeonatos filtered and shown: ${campeonatosFiltrados.size}")
                    }
                } else {
                    view.showError("Error: ${response.code()}")
                    Log.e(TAG, "Error response code: ${response.code()}")
                }
                view.hideLoading()
            }

            override fun onFailure(call: Call<List<Campeonatos>>, t: Throwable) {
                view.hideLoading()
                view.showError(t.message ?: "Error desconocido")
                Log.e(TAG, "API call failed: ${t.message}")
            }
        })
    }
}

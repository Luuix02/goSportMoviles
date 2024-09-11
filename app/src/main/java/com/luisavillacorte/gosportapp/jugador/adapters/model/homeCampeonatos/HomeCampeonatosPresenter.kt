package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import android.content.Context
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.formCrearEquipoService.CrearEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidacionResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeCampeonatosPresenter(
    private val view: HomeCampeonatosContract.View,
    private val context: Context,
    private val apiService: HomeApiService,
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

                        if (perfil.esCapitan){
                            view.mostrarBotonGestionarEquipo()
                        } else {
                            validarInscripcionJugador(perfil.id)
                        }

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
                        view.showInscripcionError("Ya estás inscrito en el equipo: ${validarInscripcionResponse.equipo[0].nombreEquipo}")
                        view.showValidacionInscripcion(true, equipo)
//                        view.mostrarBotonGestionarEquipo()
                    //                        view.navigateToGestionarEquipo(equipo)
                    } else {
                        view.showValidacionInscripcion(false, null)
//                        view.mostrarBotonGestionarEquipo()
//                        view.navigateToCrearEquipo()
                    }
                } else {
                    view.showError("Error en la respuesta del servidor.")
                }
            }

            override fun onFailure(call: Call<ValidarInscripcionResponse>, t: Throwable) {
                // Manejo de fallas en la llamada de red
                view.showError("Error al conectar con el servidor: ${t.message}")
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
                            it.estadoCampeonato == "Inscripcion"
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

    override fun validarInscripcionEquipo(identificacion: String) {
        val call = apiService.verificarEquipoEnCampeonato(identificacion)
        call.enqueue(object : Callback<ValidacionResponse> {
            override fun onResponse(
                call: Call<ValidacionResponse>,
                response: Response<ValidacionResponse>
            ) {
                if (response.isSuccessful) {
                    val validacionResponse = response.body()
                    if (validacionResponse != null) {
                        val inscripciones = validacionResponse.data.flatten()
                        val equipoInscrito = inscripciones.firstOrNull()?.equipo

                        if (equipoInscrito != null) {
                            view.showInscripcionError("Ya estás inscrito en el campeonato con el equipo: ${equipoInscrito.nombreEquipo}")
                            view.showValidacionInscripcion(true, equipoInscrito)
                        } else {
                            view.showValidacionInscripcion(false, null)
                        }
                    } else {
                        view.showError("Respuesta vacía del servidor.")
                    }
                } else {
                    view.showError("Error en la respuesta del servidor: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ValidacionResponse>, t: Throwable) {
                view.showError("Error al conectar con el servidor: ${t.message}")
            }
        })
    }

}

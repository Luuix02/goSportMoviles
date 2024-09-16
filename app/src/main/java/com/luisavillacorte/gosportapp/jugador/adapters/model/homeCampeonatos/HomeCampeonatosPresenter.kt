package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.formCrearEquipoService.CrearEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscriptoRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscriptoResponse

import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.VerificarEquipoResponse
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeCampeonatosPresenter(
    private val view: HomeCampeonatosContract.View,
    private val context: Context,
    private val apiService: HomeApiService
) : HomeCampeonatosContract.Presenter {

    private var campeonatoSeleccionado: Campeonatos? = null
    private val tokenManager = TokenManager(context)
    private val TAG = "HomePresenter"


//    override fun setCampeonatoSeleccionado(campeonato: Campeonatos) {
//        this.campeonatoSeleccionado = campeonato
//    }

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

                        if (perfil.esCapitan) {
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
                        guardarEquipoEnSharedPreferences(context, equipo.id, equipo.cedula)
                        view.showInscripcionError("Ya estás inscrito en el equipo: ${validarInscripcionResponse.equipo[0].nombreEquipo}")

                        view.showInscripcionError("Ya estás inscrito en el equipo: ${equipo.nombreEquipo}")
                        view.showValidacionInscripcion(true, equipo)
                    } else {
                        view.showValidacionInscripcion(false, null)
                    }
                } else {
                    view.showError("Error en la respuesta del servidor.")
                }
            }

            override fun onFailure(call: Call<ValidarInscripcionResponse>, t: Throwable) {
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
                            it.estadoCampeonato == "Inscripcion" || it.estadoCampeonato == "Ejecucion" || it.estadoCampeonato == "Finalizacion"
                        }

                        view.showCampeonatos(campeonatosFiltrados)
                        Log.d(TAG, "Campeonatos filtered and shown: ${campeonatosFiltrados.size}")
                    } ?: view.showError("No se recibieron campeonatos.")
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


    override fun verificarEquipoEnCampeonato(identificacion: String, callback: (Boolean) -> Unit) {
        val call = apiService.verificarEquipoEnCampeonato(identificacion)
        call.enqueue(object : Callback<VerificarEquipoResponse> {
            override fun onResponse(
                call: Call<VerificarEquipoResponse>,
                response: Response<VerificarEquipoResponse>
            ) {
                if (response.isSuccessful) {
                    val verificarResponse = response.body()
                    if (verificarResponse != null) {
                        // Verificar si el equipo está inscrito en algún campeonato
                        val equipoInscrito = verificarResponse.data.isNotEmpty() && verificarResponse.data[0].isNotEmpty()

                        // Llamar al callback con el resultado
                        callback(equipoInscrito)

                        // Mostrar mensajes en la vista si es necesario
                        if (equipoInscrito) {
                            view.showSuccess("El equipo ya está inscrito en el campeonato.")
                        } else {
                            view.showError("El equipo no está inscrito en el campeonato.")
                        }
                    } else {
                        view.showError("Respuesta vacía del servidor")
                        callback(false) // En caso de respuesta vacía, asumimos que el equipo no está inscrito
                    }
                } else {
                    view.showError("Error al verificar inscripción ${response.code()}: ${response.message()}")
                    callback(false) // En caso de error en la respuesta, asumimos que el equipo no está inscrito
                }
            }

            override fun onFailure(call: Call<VerificarEquipoResponse>, t: Throwable) {
                view.showError(t.message ?: "Error desconocido")
                callback(false) // En caso de fallo en la llamada, asumimos que el equipo no está inscrito
            }
        })

    }


    override fun inscribirEquipoEnCampeonato(idCampeonato: String) {
        val equipo = obtenerEquipoDeSharedPreferences(context)

        if (equipo != null) {
            val equipoInscriptoRequest = EquipoInscriptoRequest(
                id = equipo.id, // Usa el ID del equipo
                equipo = equipo, // Usa el equipo completo
                idCampeonato = idCampeonato
            )

            val call = apiService.inscribirEquipoCampeonato(equipoInscriptoRequest)

            call.enqueue(object : Callback<EquipoInscriptoResponse> {
                override fun onResponse(
                    call: Call<EquipoInscriptoResponse>,
                    response: Response<EquipoInscriptoResponse>
                ) {
                    if (response.isSuccessful) {
                        view.showSuccess("Equipo inscrito exitosamente en el campeonato.")
                    } else {
                        view.showError("Error al inscribir el equipo ${response.code()}: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EquipoInscriptoResponse>, t: Throwable) {
                    view.showError(t.message ?: "Error desconocido")
                }
            })
        } else {
            view.showError("No se encontró la información del equipo en las preferencias.")
        }
    }

    private fun guardarEquipoEnSharedPreferences(context: Context, equipoId: String, cedula: String) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("EQUIPO_ID", equipoId)
        editor.putString("CEDULA", cedula)
        editor.apply()
    }
    private fun obtenerEquipoDeSharedPreferences(context: Context): Equipo? {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val equipoJson = sharedPreferences.getString("EQUIPO", null)
        return if (equipoJson != null) {
            gson.fromJson(equipoJson, Equipo::class.java)
        } else {
            null
        }
    }

    }



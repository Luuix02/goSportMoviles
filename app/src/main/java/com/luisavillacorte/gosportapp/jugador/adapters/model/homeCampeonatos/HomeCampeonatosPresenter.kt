package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.gson.Gson
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.formCrearEquipoService.CrearEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.CrearEquipoResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoEstadoRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscriptoRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscriptoResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Participantes

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


    private val tokenManager = TokenManager(context)
    private val TAG = "HomePresenter"


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
                        equipo.id?.let { equipoId ->
                            guardarEquipoEnSharedPreferences(context, equipo)
                            guardarIdEquipoEnSharedPreferences(context, equipoId)
                            guardarCedulaEnSharedPreferences(context, equipo.cedula)
                        }
//                        view.showInscripcionError("Ya estás inscrito en el equipo: ${validarInscripcionResponse.equipo[0].nombreEquipo}")
                        Log.d(
                            "ValidacionInscripcion",
                            "Equipo: ${equipo.nombreEquipo}, ID: ${equipo.id}, Cédula: ${equipo.cedula}"
                        )
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
                            (it.estadoCampeonato == "Inscripcion" || it.estadoCampeonato == "Ejecucion") &&
                                    (it.tipoCampeonato == "Interfichas" || it.tipoCampeonato == "Recreativos")
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


    override fun verificarEquipoEnCampeonato(
        identificacion: String,
        callback: (Boolean, String) -> Unit
    ) {
        val call = apiService.verificarEquipoEnCampeonato(identificacion)
        call.enqueue(object : Callback<VerificarEquipoResponse> {
            override fun onResponse(
                call: Call<VerificarEquipoResponse>,
                response: Response<VerificarEquipoResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    val msg = body?.msg ?: ""
                    val data = body?.data ?: emptyList()
                    val isInscrito = data.isNotEmpty() && data.flatten().isNotEmpty()
                    callback(isInscrito, msg)
//                    view.mostrarMensaje(msg)
//                    view.mostrarEstadoInscripcion(isInscrito)
                } else {
                    callback(false, "Error en la respuesta")
//                    view.showError("Error en la respuesta del servidor: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<VerificarEquipoResponse>, t: Throwable) {
                callback(false, "Error de red")

//                view.showError("Error de red: ${t.message}")
            }

        })
    }

    override fun inscribirEquipoEnCampeonato(equipo: Equipo, idCampeonato: String) {

        Log.d("Presenter", "Inscripción: equipo=$equipo, idCampeonato=$idCampeonato")
        val equipoData = EquipoInscriptoRequest(equipo, idCampeonato)
//        // Mostrar modal de confirmación
//        view.mostrarModalConfirmacion(
//            mensaje = "¿Estás seguro de que deseas inscribir este equipo en el campeonato?",
//            onAceptar = {
//                Log.d("Presenter", "El usuario confirmó la inscripción.")
//                Log.d("Presenter", "Datos enviados: $equipoData")
//
        val call = apiService.inscribirEquipoCampeonato(equipoData)
        Log.d("Presenter", "Llamada a la API: $call")

        call.enqueue(object : Callback<EquipoInscriptoResponse> {
            override fun onResponse(
                call: Call<EquipoInscriptoResponse>,
                response: Response<EquipoInscriptoResponse>
            ) {
                Log.d(
                    "Presenter",
                    "Código de respuesta: ${response.code()}, Mensaje: ${response.message()}"
                )
                val responseBody = response.body()
                Log.d("Presenter", "Cuerpo de la respuesta: $responseBody")
                Log.d(
                    "Presenter",
                    "Código de respuesta: ${response.code()}, Mensaje: ${response.message()}, Cuerpo: ${
                        response.errorBody()?.string()
                    }"
                )
                if (response.isSuccessful) {
                    val validarInscripcionResponse = response.body()
                    if (validarInscripcionResponse != null) {
                        guardarIdCampeonatoEquipoInscrito(context, idCampeonato)
                        actualizarEstadoEquipo(equipo.id)
                        view.showSuccess("Equipo inscrito exitosamente en el campeonato.")
                    } else {
                        view.showError("Error al inscribir el equipo: ${response.code()} ${response.message()}")
                    }
                }
            }
            override fun onFailure(call: Call<EquipoInscriptoResponse>, t: Throwable) {
                Log.e("Presenter", "Error al inscribir el equipo: ${t.message}")
                view.showError(t.message ?: "Error desconocido")
            }
        })


    }

    fun actualizarEstadoEquipo(idEquipo: String?) {
        if (idEquipo != null) {
            val estadoRequest = EquipoEstadoRequest(estado = true)
            apiService.actualizarEstadoEquipo(idEquipo, estadoRequest).enqueue(object : Callback<CrearEquipoResponse> {
                override fun onResponse(call: Call<CrearEquipoResponse>, response: Response<CrearEquipoResponse>) {
                    if (response.isSuccessful) {
                        view.showSuccess("Estado del equipo actualizado a true.")
                    } else {
                        view.showError("Error al actualizar el estado: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<CrearEquipoResponse>, t: Throwable) {
                    view.showError("Fallo al actualizar el estado: ${t.message}")
                }
            })
        } else {
            view.showError("ID de equipo no disponible.")
        }
    }

}

//        val equipo = obtenerEquipoDeSharedPreferences(context)
//        if (equipo?.id != null) {
//            val equipoInscriptoRequest = EquipoInscriptoRequest(
//                id = equipo.id, // Usa el ID del equipo
//                equipo = equipo, // Usa el equipo completo
//                idCampeonato = idCampeonato
//            )
//
//
//    }




fun guardarIdCampeonatoEquipoInscrito(context: Context, idCampeonato: String) {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("CAMPEONATO_INSCRITO_ID", idCampeonato)
    editor.apply()
    Log.d("SharedPreferences", "ID guardado: $idCampeonato")
}

fun recuperarIdCampeonatoEquipoInscrito(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val idCampeonato = sharedPreferences.getString("CAMPEONATO_INSCRITO_ID", null)

    // Log para verificar que el ID fue recuperado
    Log.d("SharedPreferences", "ID recuperado: $idCampeonato")

    return idCampeonato
}


fun guardarEquipoEnSharedPreferences(context: Context, equipo: Equipo) {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val equipoJson = gson.toJson(equipo)
    editor.putString("EQUIPO_DATA", equipoJson)
    editor.apply()
    Log.d("SharedPreferences", "Equipo guardado: $equipoJson")
}

fun recuperarEquipo(context: Context): Equipo? {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val gson = Gson()
    val equipoJson = sharedPreferences.getString("EQUIPO_DATA", null)
    val equipo = gson.fromJson(equipoJson, Equipo::class.java)

    // Log para verificar que el equipo fue recuperado
    Log.d("SharedPreferences", "Equipo recuperado: $equipoJson")

    return equipo
}


fun guardarIdEquipoEnSharedPreferences(context: Context, id: String) {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("EQUIPO_ID", id)
    editor.apply()
    Log.d("SharedPreferences", "ID guardado: $id")
}

fun recuperarIdEquipo(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val id = sharedPreferences.getString("EQUIPO_ID", null)

    // Log para verificar que el ID fue recuperado
    Log.d("SharedPreferences", "ID recuperado: $id")

    return id
}

fun guardarCedulaEnSharedPreferences(context: Context, cedula: String) {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("EQUIPO_CEDULA", cedula)
    editor.apply()

    Log.d("SharedPreferences", "Cédula guardada: $cedula")
}

fun recuperarCedulaEquipo(context: Context): String? {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val cedula = sharedPreferences.getString("EQUIPO_CEDULA", null)

    // Log para verificar que la cédula fue recuperada
    Log.d("SharedPreferences", "Cédula recuperada: $cedula")

    return cedula
}


//private fun obtenerIdEquipoDeSharedPreferences(context: Context): String? {
//    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
//    val equipoId = sharedPreferences.getString("EQUIPO_ID", null)
//
//    Log.d("SharedPreferences", "Equipo ID recuperado: $equipoId")
//    return equipoId
//
//}
//
//private fun obtenerCedulaDeSharedPreferences(context: Context): String? {
//    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
//    val cedula = sharedPreferences.getString("CEDULA", null)
//
//
//    Log.d("SharedPreferences", "Cédula recuperada: $cedula")
//    return cedula
//}


//    private fun obtenerEquipoDeSharedPreferences(context: Context): Equipo? {
//        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
//        val gson = Gson()
//        val equipoJson = sharedPreferences.getString("EQUIPO", null)
//        return if (equipoJson != null) {
//            gson.fromJson(equipoJson, Equipo::class.java)
//        } else {
//            null
//        }
//    }





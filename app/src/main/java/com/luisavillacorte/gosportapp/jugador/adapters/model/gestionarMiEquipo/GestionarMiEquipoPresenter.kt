package com.luisavillacorte.gosportapp.jugador.adapters.model.gestionarMiEquipo

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.gestionarMiEquipoService.GestionarMiEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Participante
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.UploadResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.http.Path
import java.io.File

class GestionarMiEquipoPresenter(

    private val view: GestionarMiEquipoContract.View,
    private val apiService: GestionarMiEquipoApiService,
    private val context: Context,

    ) : GestionarMiEquipoContract.Presenter {

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        if (currentQuery.isNotEmpty()) {
            realizarBusqueda(currentQuery)
        } else {
            view.showJugadores(emptyList())
        }
    }

    private var currentQuery: String = ""
    fun getFileFromUri(uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File.createTempFile("temp", ".jpg", context.cacheDir)
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    private val tokenManager = TokenManager(context)
    private val TAG = "GestionarMiEquipoPresenter"
    override fun actualizarEquipo(equipo: Equipo, equipoId: String) {
        val call = apiService.actualizarEquipo(equipoId, equipo)
        call.enqueue(object : Callback<Equipo> {
            override fun onResponse(call: Call<Equipo>, response: Response<Equipo>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        view.mostrarActualizacionExitosa(it)
                    } ?: view.showError("No se recibió respuesta del servidor")
                } else {
                    when (response.code()) {
                        403 -> view.showError("No tienes permiso para actualizar este equipo")
                        404 -> view.showError("Equipo no encontrado")
                        else -> view.showError("Error al actualizar el equipo: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<Equipo>, t: Throwable) {
                view.showError("Error en la solicitud: ${t.message}")
            }
        })
    }

    override fun validarInscripcion(idJugador: String) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val call = apiService.validarInscripcionIntegrante(idJugador)

        call.enqueue(object : Callback<ValidarInscripcionResponse> {
            override fun onResponse(
                call: Call<ValidarInscripcionResponse>,
                response: Response<ValidarInscripcionResponse>
            ) {
                if (response.isSuccessful) {
                    val validarResponse = response.body()
                    if (validarResponse != null) {
                        if (validarResponse.equipo.isNotEmpty()) {
                            // Si el jugador ya está inscrito en un equipo
                            view.showError("El jugador ya está inscrito en un equipo: ${validarResponse.equipo[0].nombreEquipo}")
                        } else {
                            // Si el jugador no está inscrito en ningún equipo
                            view.showValidacionExitosa(idJugador)
                        }
                    } else {
                        view.showError("Respuesta vacía del servidor")
                    }
                } else {
                    view.showError("Error al validar inscripción ${response.code()}: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ValidarInscripcionResponse>, t: Throwable) {
                view.showError(t.message ?: "Error desconocido")
            }
        })
    }

    override fun realizarBusqueda(query: String) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val call = apiService.buscarJugadoresPorIdent("Bearer $token", query)
        Log.d(TAG, "Token enviado: Bearer $token")
        Log.d(TAG, "Identificación enviada: $query")

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

                view.showLoading(false)

                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    users.forEach { user ->
                        Log.d("CrearEquipoPresenter", "Datos del usuario: $user")
                        Log.d("CrearEquipoPresenter", "Rol del usuario: ${user.rol.trim()}")
                    }
                    Log.d("CrearEquipoPresenter", "Usuarios recibidos: $users")
                    val jugador = users.filter { user ->
                        user.rol.trim().equals("jugador", ignoreCase = true)
                    }
                    Log.d("CrearEquipoPresenter", "Usuarios filtrados como jugadores: $jugador")
                    val participantes = jugador.map { user ->
                        Participante(
                            id = user.id,
                            nombreJugador = user.nombres,
                            ficha = user.ficha ?: "",
                            dorsal = ""
                        )
                    }
                    view.showJugadores(jugador)
                } else {
                    view.showError("Error al buscar jugadores")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                view.showLoading(false)
                Log.e(TAG, "Error en la solicitud: ${t.message}")
//                view.showError("Error en la solicitud: ${t.message}")
            }
        })
    }

    override fun actualizarLogoEquipo(id: String, idLogo: String, file: MultipartBody.Part) {
        view.showLoading(true)
        Log.d("Presenter", "Subiendo logo para equipoId: $id, idLogo: $idLogo")
        val call = apiService.actualizarLogoEquipo(id, idLogo, file)
        call.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                view.showLoading(false)
                if (response.isSuccessful) {
                    Log.d("Presenter", "Logo actualizado exitosamente")
                    val uploadResponse = response.body()
                    if (uploadResponse != null) {
                        view.mostrarActualizacionLogoExitosa(
                            uploadResponse.message,
                            uploadResponse.url
                        )
                    } else {
                        view.showError("Respuesta exitosa, pero no se recibió un mensaje o URL")
                    }
                } else {
                    Log.e("Presenter", "Error: ${response.code()} ${response.message()}")
                    when (response.code()) {
                        403 -> view.showError("No tienes permiso para actualizar el logo")
                        404 -> view.showError("Equipo no encontrado")
                        500 -> view.showError("Error interno del servidor")
                        else -> view.showError("Error al actualizar el logo: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.e("Presenter", "Error en la solicitud: ${t.message}")
                view.showError("Error en la solicitud: ${t.message}")
                view.showLoading(false)
            }
        })


    }
}


package com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.formCrearEquipoService.CrearEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CrearEquipoPresenter(

    private val view: CrearEquipoContract.View,
    private val context: Context,
    private val apiService: CrearEquipoApiService


) : CrearEquipoContract.Presenter {


    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        if (currentQuery.isNotEmpty()) {
            realizarBusqueda(currentQuery)
        } else {
            view.showJugadores(emptyList())
        }
    }

    private var currentQuery: String = ""

    private val tokenManager = TokenManager(context)
    private val TAG = "CrearEquipoPresenter"
//    private var userCedula: String? = null

    fun getFileFromUri(uri: Uri): File?{
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File.createTempFile("temp", ".jpg", context.cacheDir)
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    override fun validarInscripcion(idJugador: String) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val call = apiService.validarInscripcionIntegrante(idJugador)

        call.enqueue(object : Callback<ValidarInscripcionResponse> {
            override fun onResponse(call: Call<ValidarInscripcionResponse>, response: Response<ValidarInscripcionResponse>) {
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
    override fun subirLogoEquipo(userId: String, uri: Uri, equipo: Equipo) {
        val file = getFileFromUri(uri) ?: run {
            view.showError("No se pudo obtener el archivo de la imagen")
            return
        }
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        apiService.subirLogoEquipo(userId, body).enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    val imgLogo = uploadResponse?.url ?: ""
                    val idLogo = uploadResponse?.public_id ?: ""
                    Log.d("SubirLogo", "imgLogo: $imgLogo, idLogo: $idLogo")

                    val equipoActualizado = equipo.copy(imgLogo = imgLogo, idLogo = idLogo)
                    Log.d(TAG, "Equipo actualizado para crear: $equipoActualizado")
                    crearEquipo(equipoActualizado)
                } else {
                    view.showError("Error al subir la imagen. Código de respuesta: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                view.showError("Error de red: ${t.message}")
            }
        })
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
//                       userCedula = perfil.identificacion
                        view.showPerfilUsuario(perfil)
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



    override fun crearEquipo(equipo: Equipo) {
        if (validateEquipo(equipo)) {
            val call = apiService.crearEquipo(equipo)
            call.enqueue(object : Callback<CrearEquipoResponse> {
                override fun onResponse(
                    call: Call<CrearEquipoResponse>,
                    response: Response<CrearEquipoResponse>
                ) {
                    if (response.isSuccessful) {
                        view.showSuccess("Equipo creado exitosamente")
                    } else {
                        view.showError("Error al crear el equipo ${response.code()}: ${response.message()}")
                    }
                }


                override fun onFailure(call: Call<CrearEquipoResponse>, t: Throwable) {
                    view.showError(t.message ?: "Error desconocido")
                }
            })
        } else {
            view.showError("Por favor, complete todos los campos del equipo")
        }
    }


    private fun validateEquipo(equipo: Equipo): Boolean {
        return equipo.nombreEquipo.isNotEmpty() &&
                equipo.nombreCapitan.isNotEmpty() &&
                equipo.contactoUno.isNotEmpty() &&
                equipo.contactoDos.isNotEmpty() &&
                equipo.participantes.size >= 4 &&
                equipo.imgLogo.isNotEmpty() &&
                equipo.idLogo.isNotEmpty() &&
                equipo.puntos >= 0
    }

    override fun buscarJugadores(identificacion: String) {

        view.showLoading(true)

        currentQuery = identificacion

        handler.removeCallbacks(searchRunnable)

        handler.postDelayed(searchRunnable, 300)
    }

    private fun realizarBusqueda(query: String) {
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
}

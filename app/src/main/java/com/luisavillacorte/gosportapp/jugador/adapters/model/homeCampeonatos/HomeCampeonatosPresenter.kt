package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class HomeCampeonatosPresenter(
    private val view: HomeCampeonatosContract.View,
    private val context: Context,
    private val apiService: HomeApiService
) : HomeCampeonatosContract.Presenter {

    private val tokenManager = TokenManager(context)
    private val TAG = "HomePresenter"

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

    fun actualizarPerfilUsuario(perfilActualizado: PerfilUsuarioResponse) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val userId = tokenManager.getUserId() ?: return view.showError("User ID no disponible")

        val call = apiService.actualizarPerfilUsuario("Bearer $token", userId, perfilActualizado)
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
    }

    fun cambiarContrasena(nuevaContrasenaRequest: NuevaContrasenaRequest) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val userId = tokenManager.getUserId() ?: return view.showError("User ID no disponible")

        val call = apiService.cambiarContrasena("Bearer $token", userId, nuevaContrasenaRequest)
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
    }

    override fun subirFoto(uri: Uri) {
        Log.d(TAG, "Iniciando subida de foto con URI: $uri")

        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val userId = tokenManager.getUserId() ?: return view.showError("User ID no disponible")

        try {
            val file = File(getFilePathFromUri(uri) ?: return view.showError("No se pudo obtener la ruta del archivo"))
            if (!file.exists()) {
                Log.e(TAG, "El archivo no existe en la URI proporcionada")
                return view.showError("No se pudo encontrar el archivo de imagen")
            }

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val call = apiService.subirFotousuario(userId, "Bearer $token", body)
            call.enqueue(object : Callback<PerfilUsuarioResponse> {
                override fun onResponse(call: Call<PerfilUsuarioResponse>, response: Response<PerfilUsuarioResponse>) {
                    if (response.isSuccessful) {
                        val perfilActualizado = response.body()
                        if (perfilActualizado != null) {
                            view.traernombre(perfilActualizado)
                            view.showSuccess("Foto de perfil actualizada correctamente")
                        } else {
                            view.showError("Error al actualizar la foto de perfil")
                        }
                    } else {
                        view.showError("Error en la respuesta: ${response.code()}: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                    view.showError(t.message ?: "Error desconocido al subir la foto")
                }
            })
        } catch (e: Exception) {
            Log.e(TAG, "Error al procesar la URI de imagen", e)
            view.showError("Error al procesar la URI de imagen")
        }
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        }
        return null
    }

    override fun validarInscripcionJugador(idJugador: String) {
        Log.d(TAG, "validarInscripcionJugador aún no implementado")
        view.showError("Funcionalidad no disponible aún")
    }

    override fun validarInscripcionEquipo(identificacion: String) {
        // Implementar la lógica para validar la inscripción del equipo
        Log.d(TAG, "validarInscripcionEquipo aún no implementado")
        view.showError("Funcionalidad no disponible aún")
    }
}

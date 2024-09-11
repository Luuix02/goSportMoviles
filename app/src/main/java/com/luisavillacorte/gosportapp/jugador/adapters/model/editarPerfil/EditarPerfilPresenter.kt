package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.editarPerfil

import android.content.Context
import android.net.Uri
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.editarPerfil.EditarPerfilContract
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditarPerfilPresenter(
    private val view: EditarPerfilContract.View,
    private val context: Context,
    private val apiService: HomeApiService
) : EditarPerfilContract.Presenter {

    private val tokenManager = TokenManager(context)
    private val token: String = tokenManager.getToken() ?: throw IllegalStateException("Token no disponible")

    override fun getPerfilUsuario() {
        view.showLoading()
        apiService.obtenerPerfilUsuario(token).enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(call: Call<PerfilUsuarioResponse>, response: Response<PerfilUsuarioResponse>) {
                view.hideLoading()
                if (response.isSuccessful) {
                    response.body()?.let {
                        view.traernombre(it)
                    }
                } else {
                    view.showError("Error al obtener perfil")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.hideLoading()
                view.showError("Error: ${t.message}")
            }
        })
    }

    override fun actualizarPerfilUsuario(id: String, perfilUsuarioResponse: PerfilUsuarioResponse) {
        view.showLoading()
        apiService.actualizarPerfilUsuario(token, id, perfilUsuarioResponse).enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(call: Call<PerfilUsuarioResponse>, response: Response<PerfilUsuarioResponse>) {
                view.hideLoading()
                if (response.isSuccessful) {
                    view.showSuccess("Perfil actualizado con éxito")
                } else {
                    view.showError("Error al actualizar perfil")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.hideLoading()
                view.showError("Error: ${t.message}")
            }
        })
    }

    override fun subirFoto(uri: Uri, idUser: String) {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: run {
            view.showError("No se puede abrir el archivo")
            return
        }

        val file = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }

        val requestFile = file.asRequestBody("image/*".toMediaType())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        apiService.subirFotousuario(idUser, token, body).enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(call: Call<PerfilUsuarioResponse>, response: Response<PerfilUsuarioResponse>) {
                if (response.isSuccessful) {
                    view.showSuccess("Foto subida con éxito")
                } else {
                    view.showError("Error al subir foto")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.showError("Error: ${t.message}")
            }
        })
    }
}

package com.luisavillacorte.gosportapp.planillero.adpaters.model.EditarCuentaPlanillero

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.CloudinaryResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import com.luisavillacorte.gosportapp.planillero.adpaters.api.EditarCuentaPlanillero.ApiServiceEdiarCuenta
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PresenterEditarCuentaPlanillero (
    private val view: ContractEditarCuentaPlanillero.View,
    private val context: Context,
    private val apiService: ApiServiceEdiarCuenta,

): ContractEditarCuentaPlanillero.Presenter{
    private val tokenManager = TokenManager(context)

    override fun updatePerfilPlanillero(id: String, actualizar: DatosPlanilleroActualizar) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible");
         val call = apiService.actualizarDatosPlanillero(id, "Bearer ${token}", actualizar)

        call.enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(
                call: Call<PerfilUsuarioResponse>,
                response: Response<PerfilUsuarioResponse>
            ) {
               if (response.isSuccessful){
                   val perfilActualizado = response.body()
                   if (perfilActualizado!= null){
                       view.showSuccess("Perfil actualizado correctamente")
                       view.PerfilPlanillero(perfilActualizado);
                   }
               }else{
                   view.showError("Error al actualizar el perfil")
               }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
               view.error("ERROR DE CONEXION")
            }
        })
    }
    override fun obtenerPerfilUsuario() {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        Log.d(TAG, "Token Planillero: $token")
        val call = apiService.getPerfilPlanillero("Bearer $token")
        call.enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(
                call: Call<PerfilUsuarioResponse>,
                response: Response<PerfilUsuarioResponse>
            ) {
                if(response.isSuccessful){
                    val perfil = response.body()
                    if(perfil!=null){
                        saveUserId(context, perfil.id)
                        view.PerfilPlanillero(perfil)
                    }
                }else{
                    view.showError("Error al obtener el perfil")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.showError("Error de conexión")
            }
        })
    }
    fun saveUserId(context: Context, id: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER_ID", id)
        editor.apply()
    }




    override fun subirFoto(id: String, foto: String) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val file = File(foto)
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val call = apiService.subirFotoPlanillero(id, "Bearer $token", body)
        call.enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(
                call: Call<PerfilUsuarioResponse>,
                response: Response<PerfilUsuarioResponse>
            ) {
                if (response.isSuccessful) {
                    val perfilActualizado = response.body()
                    if (perfilActualizado != null) {
                        view.PerfilPlanillero(perfilActualizado)

                        obtenerPerfilUsuario()
                    }else{
                        view.showError("Respuesta vacía del servidor")
                    }
                } else {
                    view.showError("Error al subir la foto")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.error("Error de conexión")
            }
        })
    }

        override fun actualizarFoto(id: String, fotoPath: String, public_Id: String) {
            val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
            // Verifica si el archivo existe
            val file = File(fotoPath)
            if (!file.exists()) {
                return view.showError("Archivo no encontrado")
            }

            // Prepara el archivo para la solicitud
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile);            val requestBody = CloudinaryResponse(public_Id, fotoPath)

            val call = apiService.actualizarFotoPlanillero(id, "Bearer $token", body)
            call.enqueue(object : Callback<PerfilUsuarioResponse> {
                override fun onResponse(
                    call: Call<PerfilUsuarioResponse>,
                    response: Response<PerfilUsuarioResponse>
                ) {
                    if (response.isSuccessful) {
                        val perfilActualizado = response.body()
                        if (perfilActualizado != null) {
                            view.PerfilPlanillero(perfilActualizado)
                            obtenerPerfilUsuario()
                        } else {
                            view.showError("Respuesta vacía del servidor")
                        }
                    } else {
                        view.showError("Error al actualizar la foto")
                    }
                }
                override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                    view.showError("Error de conexión")
                }
            })
        }

    override fun eliminarFoto( id: String, public_Id: String) {
         val token = tokenManager.getToken() ?: return view.showError("Error al obtener token");


            if (public_Id.isBlank()) {
                view.showError("No hay foto para eliminar")
                return
            }

            val call = apiService.eliminarFotoPlanillero(id, "Bearer $token", public_Id)
            call.enqueue(object : Callback<PerfilUsuarioResponse> {
                override fun onResponse(
                    call: Call<PerfilUsuarioResponse>,
                    response: Response<PerfilUsuarioResponse>
                ) {
                    if (response.isSuccessful) {
                        val perfilActualizado = response.body()
                        if (perfilActualizado != null) {
                            view.showSuccess("Foto eliminada correctamente")
                            obtenerPerfilUsuario()
                        } else {
                            view.showError("Hubo un error")
                        }
                    } else {
                        view.showError("Error al eliminar foto")
                    }
                }

                override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                    view.showError("Error de conexión")
                }
            })
 
    }


}

package com.luisavillacorte.gosportapp.jugador.adapters.model.cambiarContrasena

import android.content.Context
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CambiarContrasenaPresenter(
    private val view: CambiarContrasenaContract.View,
    private val context: Context,
    private val apiService: HomeApiService,

): CambiarContrasenaContract.Presenter  {

    private val tokenManager = TokenManager(context)
    private val TAG = "Cambiar contrasena presenter"

    fun cambiarContrasena(nuevaContrasenaRequest: NuevaContrasenaRequest) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val userId = tokenManager.getUserId()
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

}
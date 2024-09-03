package com.luisavillacorte.gosportapp.jugador.adapters.model.perfil

import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.perfilJugador.PlayerApiService
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePresenter(
    private val view: ProfileContract.View,
    private val apiService: PlayerApiService,
    private val tokenManager: TokenManager
) : ProfileContract.Presenter {

    override fun loadProfile() {
        val token = tokenManager.getToken() // Obtén el token guardado
        val call = apiService.getPlayerProfile("Bearer $token") // Añade el token al header
        call.enqueue(object : Callback<List<PlayerProfile>> {
            override fun onResponse(call: Call<List<PlayerProfile>>, response: Response<List<PlayerProfile>>) {
                Log.d("ProfilePresenter", "Código de estado: ${response.code()}")
                Log.d("ProfilePresenter", "Cuerpo de la respuesta: ${response.body()}")
                Log.d("ProfilePresenter", "Mensaje de respuesta: ${response.message()}")
                if (response.isSuccessful) {
                    val profiles = response.body()
                    if (profiles != null && profiles.isNotEmpty()) {
                        Log.d("ProfilePresenter", "Perfil encontrado: ${profiles[0].nombres}")
                        view.showProfile(profiles[0])
                    } else {
                        Log.d("ProfilePresenter", "No se encontraron perfiles")
                        view.showError("No se encontraron perfiles")
                    }
                } else {
                    Log.e("ProfilePresenter", "Error al cargar el perfil: ${response.errorBody()?.string()}")
                    view.showError("Error al cargar el perfil")
                }
            }

            override fun onFailure(call: Call<List<PlayerProfile>>, t: Throwable) {
                Log.e("ProfilePresenter", "Fallo en la conexión: ${t.message}")
                view.showError("Fallo en la conexión: ${t.message}")
            }
        })
    }
}

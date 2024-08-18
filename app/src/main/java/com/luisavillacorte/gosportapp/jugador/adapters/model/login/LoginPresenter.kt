package com.luisavillacorte.gosportapp.jugador.adapters.model.login

import android.content.Context
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.authService.ApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.AuthResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.LoginCredentials
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenter(

    private val view: LoginContract.View,
    private val apiService: ApiService,
    private val context: Context
)  : LoginContract.Presenter{


        private val tokenManager = TokenManager(context)

        override fun loginUser(correo: String, contrasena: String) {
            view.showLoading()
            Log.d("LoginPresenter", "Starting login request for user: $correo")


            val loginRequest =
                LoginCredentials(
                    correo,
                    contrasena
                )
            apiService.loginUser(loginRequest).enqueue(object : Callback<AuthResponse> {
                override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                    view.hideLoading()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            if (it.success) {
                                it.token?.let { token ->
                                    val rol = it.user.rol
                                    if (rol == "planillero" || rol == "jugador") {
                                        Log.d("LoginPresenter", "Login successful. Token: $token")
                                        tokenManager.saveToken(token)

                                        redirectUserBasedOnRole(rol)


                                    } else {
                                        Log.d("LoginPresenter", "Access denied for role: $rol")
                                        view.showError("Acceso denegado. Solo jugadores y planilleros pueden ingresar.")
                                    }

                                } ?: run {
                                    view.showError("Token no disponible")
                                }
                            } else {
                                // Aquí solo mostramos el mensaje amigable sin el código de estado HTTP
                                val errorMessage = it.message ?: "Error desconocido al iniciar sesión"
                                Log.e("LoginPresenter", "Login error: $errorMessage")
                                view.showError(errorMessage)
                            }
                        } ?: run {
                            val errorMessage = "Respuesta del servidor vacía"
                            Log.e("LoginPresenter", errorMessage)
                            view.showError(errorMessage)
                        }
                    } else {
                        val errorMessage = "Error al iniciar sesión: ${response.message()}"
                        Log.e("LoginPresenter", errorMessage)
                        view.showError("Verifica tus credenciales e intenta nuevamente.")
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    view.hideLoading()
                    val errorMessage = "Error de red: ${t.message}"
                    Log.e("LoginPresenter", errorMessage)
                    view.showError(errorMessage)
                }
            })

        }

        private fun redirectUserBasedOnRole(rol: String) {
            when (rol) {
                "planillero" -> view.navigateToPlanillero()
                "jugador" -> view.navigateToJugador()
            }
        }

    }

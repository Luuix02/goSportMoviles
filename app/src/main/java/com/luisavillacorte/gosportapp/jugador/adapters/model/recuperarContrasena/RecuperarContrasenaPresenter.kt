package com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena

import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.recuperarContrasena.RecuperarContrasenaApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.AuthResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.cambiarContrasena.CambiarContrasenaContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class RecuperarContrasenaPresenter(
    private val apiService: RecuperarContrasenaApiService,
    private var solicitarCodigoView: RecuperarContrasenaContract.SolicitarCodigoView? = null,
    private var verificarCodigoView: RecuperarContrasenaContract.VerificarCodigoView? = null,
    private var cambiarContrasenaView: RecuperarContrasenaContract.CambiarContrasenaView? = null
) : RecuperarContrasenaContract.Presenter {

    override fun solicitarCodigo(correo: String) {

        if (!isValidEmail(correo)) {
            solicitarCodigoView?.showError("Correo inválido")
            return
        }

        val request = SolicitarCodigoRequest(correo)
        apiService.solicitarCodigo(request).enqueue(object : Callback<SolicitarCodigoResponse> {
            override fun onResponse(
                call: Call<SolicitarCodigoResponse>,
                response: Response<SolicitarCodigoResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.message == "Código de verificación enviado por correo.") {
                        solicitarCodigoView?.navigateToVerificationScreen(correo)
                    } else {
                        solicitarCodigoView?.showError(responseBody?.message ?: "Correo no registrado")
                    }
                } else {
                    solicitarCodigoView?.showError("Correo no registrado")
                }
            }

            override fun onFailure(call: Call<SolicitarCodigoResponse>, t: Throwable) {
                solicitarCodigoView?.showError("Error de conexión")
            }
        })
    }

    override fun verificarCodigo(correo: String, codigo: String) {
        val request = VerificarCodigoRequest(correo, codigo)
        apiService.validarCodigo(request).enqueue(object : Callback<VerificarCodigoResponse> {
            override fun onResponse(
                call: Call<VerificarCodigoResponse>,
                response: Response<VerificarCodigoResponse>
            ) {
                if (response.isSuccessful) {
                    verificarCodigoView?.navigateToChangePasswordScreen(correo)
                } else {
                    verificarCodigoView?.showError(response.body()?.message ?: "Código incorrecto o expirado")
                }
            }

            override fun onFailure(call: Call<VerificarCodigoResponse>, t: Throwable) {
                verificarCodigoView?.showError("Error de conexión: ${t.message}")
            }
        })
    }

    override fun cambiarContrasena(correo: String, nuevaContrasena: String) {
        Log.d("CambiarContrasena", "Correo: $correo, NuevaContraseña: $nuevaContrasena")
        if (!isValidPassword(nuevaContrasena)) {
            cambiarContrasenaView?.showError("Contraseña no válida")
            return
        }
        cambiarContrasenaView?.showLoading()
        val request = CambiarContrasenaRequest(correo, nuevaContrasena)
        apiService.cambiarContrasena(request).enqueue(object : Callback<CambiarContrasenaResponse> {
            override fun onResponse(
                call: Call<CambiarContrasenaResponse>,
                response: Response<CambiarContrasenaResponse>
            ) {
                cambiarContrasenaView?.hideLoading()
                Log.d("CambiarContrasena", "Response: ${response.code()}, ${response.body()?.message}")
                if (response.isSuccessful) {
                    cambiarContrasenaView?.showSuccess("Contraseña cambiada con éxito")
                    cambiarContrasenaView?.navigateToLoginScreen()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error al cambiar la contraseña"
                    cambiarContrasenaView?.showError(errorMessage)
                    Log.e("CambiarContrasena", "Error al cambiar: $errorMessage")
                }
            }

            override fun onFailure(call: Call<CambiarContrasenaResponse>, t: Throwable) {
                cambiarContrasenaView?.hideLoading()
                cambiarContrasenaView?.showError("Error de conexión: ${t.message}")
                Log.e("CambiarContrasena", "Error de conexión", t)
            }
        })
    }


    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}

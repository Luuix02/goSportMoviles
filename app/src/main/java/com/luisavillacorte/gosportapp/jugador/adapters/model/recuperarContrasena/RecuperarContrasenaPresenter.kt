package com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena

import com.luisavillacorte.gosportapp.jugador.adapters.apiService.recuperarContrasena.RecuperarContrasenaApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.AuthResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.cambiarContrasena.CambiarContrasenaContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class RecuperarContrasenaPresenter(
//    private  val view: RecuperarContrasenaContract.View,
//    private val apiService: RecuperarContrasenaApiService
//) : RecuperarContrasenaContract.Presenter {
//
//    override fun solicitarCodigo(correo: String) {
//        view.showLoading()
//        val request = SolicitarCodigoRequest(correo)
//        apiService.solicitarCodigo(request).enqueue(object : Callback<SolicitarCodigoResponse> {
//            override fun onResponse(
//                call: Call<SolicitarCodigoResponse>,
//                response: Response<SolicitarCodigoResponse>
//            ) {
//                view.hideLoading()
//                if (response.isSuccessful) {
//                    val message = response.body()?.message ?: "Código enviado con éxito."
//                    view.showSuccess(message)
//                    // Navegar a la pantalla de verificación del código
//                    view.navigateToVerificationScreen(correo)
//                } else {
//                    view.showError("Error al enviar el código.")
//                }
//            }
//
//            override fun onFailure(call: Call<SolicitarCodigoResponse>, t: Throwable) {
//                view.hideLoading()
//                view.showError("Error en la conexión.")
//            }
//        })
//    }
//
//
//    override fun verificarCodigo(correo: String, codigo: String) {
//        view.showLoading()
//        val request = VerificarCodigoRequest(correo, codigo)
//        apiService.validarCodigo(request).enqueue(object : Callback<VerificarCodigoResponse> {
//            override fun onResponse(
//                call: Call<VerificarCodigoResponse>,
//                response: Response<VerificarCodigoResponse>
//            ) {
//                view.hideLoading()
//                if (response.isSuccessful) {
//                    val message = response.body()?.message ?: "Código verificado con éxito."
//                    view.showSuccess(message)
//                    view.showNewPasswordInput() // Cambiar a la pantalla para ingresar la nueva contraseña
//                } else {
//                    view.showError("Código de verificación inválido.")
//                }
//            }
//
//            override fun onFailure(call: Call<VerificarCodigoResponse>, t: Throwable) {
//                view.hideLoading()
//                view.showError("Error en la conexión.")
//            }
//        })
//    }
//
//
//
//        override fun cambiarContrasena(correo: String, nuevaContrasena: String) {
//            view.showLoading()
//            val request = CambiarContrasenaRequest(correo, nuevaContrasena)
//            apiService.cambiarContrasena(request)
//                .enqueue(object : Callback<CambiarContrasenaResponse> {
//                    override fun onResponse(
//                        call: Call<CambiarContrasenaResponse>,
//                        response: Response<CambiarContrasenaResponse>
//                    ) {
//                        view.hideLoading()
//                        if (response.isSuccessful) {
//                            val message =
//                                response.body()?.message ?: "Contraseña cambiada con éxito."
//                            view.showSuccess(message)
//                        } else {
//                            view.showError("Error al cambiar la contraseña.")
//                        }
//                    }
//
//                    override fun onFailure(call: Call<CambiarContrasenaResponse>, t: Throwable) {
//                        view.hideLoading()
//                        view.showError("Error en la conexión.")
//                    }
//                })
//        }
//    }
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
                    solicitarCodigoView?.navigateToVerificationScreen(correo)
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
                    verificarCodigoView?.showError("Código incorrecto o expiradp")
                }
            }

            override fun onFailure(call: Call<VerificarCodigoResponse>, t: Throwable) {
                verificarCodigoView?.showError("Error de conexión")
            }
        })
    }

    override fun cambiarContrasena(correo: String, nuevaContrasena: String) {
        if (!isValidPassword(nuevaContrasena)) {
            cambiarContrasenaView?.showError("Contraseña no válida")
            return
        }

        val request = CambiarContrasenaRequest(correo, nuevaContrasena)
        apiService.cambiarContrasena(request).enqueue(object : Callback<CambiarContrasenaResponse> {
            override fun onResponse(
                call: Call<CambiarContrasenaResponse>,
                response: Response<CambiarContrasenaResponse>
            ) {
                if (response.isSuccessful) {
                    cambiarContrasenaView?.navigateToLoginScreen()
                } else {
                    cambiarContrasenaView?.showError("Error al cambiar la contraseña")
                }
            }

            override fun onFailure(call: Call<CambiarContrasenaResponse>, t: Throwable) {
                cambiarContrasenaView?.showError("Error de conexión")
            }
        })
    }


    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}

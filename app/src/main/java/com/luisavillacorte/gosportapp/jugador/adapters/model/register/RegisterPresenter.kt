package com.luisavillacorte.gosportapp.jugador.adapters.model.register

import android.util.Log
import androidx.leanback.widget.Presenter
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.authService.ApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.AuthResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegisterPresenter :  RegisterContract.Presenter {

    private var view: RegisterContract.View? = null
    private var firstSectionData: Map<String, String> = emptyMap()
    private var secondSectionData: Map<String, String> = emptyMap()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val authService: ApiService = RetrofitInstance.createService(
        ApiService::class.java)


    override fun attachView(view: RegisterContract.View) {
        this.view =  view
    }

    override fun detachView() {
        this.view = null
    }

    override fun checkEmailExists(correo: String, callback: (Boolean) -> Unit) {
        val existsEmail = false
        callback(existsEmail)
    }

    override fun checkIdentificationExists(identificacion: String, callback: (Boolean) -> Unit) {
        val exists = false
        callback(exists)
    }

    override fun saveFirstSectionData(data: Map<String, String>) {
        firstSectionData = data.toMutableMap()
        view?.showSecondSection()
    }

    override fun saveSecondSectionData(data: Map<String, String>) {
        secondSectionData = data.toMutableMap()
    }



    override fun registerUser() {
        val combinedData = mutableMapOf<String, String>().apply {
            putAll(firstSectionData)
            putAll(secondSectionData)
        }

        val finFichaDate:  Date? = try {
            dateFormat.parse(combinedData["finFicha"] ?: "")
        } catch (e: Exception) {
            null
        }

        val user = User(
            nombres = combinedData["nombres"] ?: "",
            identificacion = combinedData["identificacion"] ?: "",
            telefono = combinedData["celular"] ?: "",
            jornada = combinedData["jornada"] ?: "",
            programa = combinedData["programa"] ?: "",
            ficha = combinedData["numFicha"] ?: "",
            finFicha = finFichaDate ?: Date(),
            correo = combinedData["correo"] ?: "",
            contrasena = combinedData["contrasena"] ?: "",
            rol = combinedData["rol"] ?: "",
            dorsal = combinedData["dorsal"] ?: "",

            )

        authService.registerUser(user).enqueue(object :
            Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                if (response.isSuccessful){
                    view?.showSuccess()
                } else {
                    Log.e("RegisterPresenter", "Error de registro: ${response.errorBody()?.string()}")
                    view?.showError("Error al registrar el usuario: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                view?.showError("Error de red: ${t.message}")
            }
        })

    }

}

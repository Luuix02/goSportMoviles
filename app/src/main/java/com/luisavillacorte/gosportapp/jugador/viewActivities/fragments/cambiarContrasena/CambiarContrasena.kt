package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.cambiarContrasena

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.cambiarContrasena.CambiarContrasenaContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.cambiarContrasena.CambiarContrasenaPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosPresenter

class CambiarContrasena : Fragment(), CambiarContrasenaContract.View{

    private var isCurrentPasswordVisible = false
    private var isNewPasswordVisible = false
    private var isConfirmNewPasswordVisible = false
    private lateinit var presenter: CambiarContrasenaPresenter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cambiar_contrasena, container, false)

        val etCurrentPassword = view.findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPassword = view.findViewById<EditText>(R.id.etNewPassword)
        val etConfirmNewPassword = view.findViewById<EditText>(R.id.etConfirmNewPassword)
        val btnGuardarCambios = view.findViewById<Button>(R.id.btnguardarcambios)



        // Inicializar el presenter
        val apiService = RetrofitInstance.createService(HomeApiService::class.java)
        presenter = CambiarContrasenaPresenter(this, requireContext(), apiService)

        btnGuardarCambios.setOnClickListener {
            val nuevaContrasena = etNewPassword.text.toString()
            val confirmarContrasena = etConfirmNewPassword.text.toString()

            if (nuevaContrasena == confirmarContrasena) {
                val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("user_id", null)

                Log.d("CambiarContrasena", "User ID recuperado: $userId")

                if (userId != null) {
                    val nuevaContrasenaRequest = NuevaContrasenaRequest(nuevaContrasena)
//                    presenter(nuevaContrasenaRequest)
                } else {
                    showError("User ID no disponible")
                }
            } else {
                showError("Las contraseñas no coinciden")
            }
        }



        return view
    }

    private fun togglePasswordVisibility(editText: EditText, isPasswordVisible: Boolean): Boolean {
        if (isPasswordVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ver_contra, 0)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ver_contra, 0) // Asegúrate de usar el drawable correcto
        }
        editText.setSelection(editText.text.length)
        return !isPasswordVisible
    }

    override fun showSuccess(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


}

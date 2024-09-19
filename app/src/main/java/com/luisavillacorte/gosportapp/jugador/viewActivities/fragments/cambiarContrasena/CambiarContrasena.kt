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
import androidx.fragment.app.Fragment
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosPresenter

class CambiarContrasena : Fragment(), HomeCampeonatosContract.View {

    private var isCurrentPasswordVisible = false
    private var isNewPasswordVisible = false
    private var isConfirmNewPasswordVisible = false
    private lateinit var presenter: HomeCampeonatosPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cambiar_contrasena, container, false)

        val etCurrentPassword = view.findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPassword = view.findViewById<EditText>(R.id.etNewPassword)
        val etConfirmNewPassword = view.findViewById<EditText>(R.id.etConfirmNewPassword)
        val btnGuardarCambios = view.findViewById<Button>(R.id.btnguardarcambios)
        val ivTogglePassword = view.findViewById<ImageView>(R.id.ivTogglePasswordNew)

        // Inicializar el presenter
        val apiService = RetrofitInstance.createService(HomeApiService::class.java)
        presenter = HomeCampeonatosPresenter(this, requireContext(), apiService)

        btnGuardarCambios.setOnClickListener {
            val nuevaContrasena = etNewPassword.text.toString()
            val confirmarContrasena = etConfirmNewPassword.text.toString()

            if (nuevaContrasena.isNotBlank() && confirmarContrasena.isNotBlank()) {
                if (nuevaContrasena == confirmarContrasena) {
                    val nuevaContrasenaRequest = NuevaContrasenaRequest(nuevaContrasena)

                    presenter.cambiarContrasena(nuevaContrasenaRequest)
                } else {
                    Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Toggle visibility of password fields
        ivTogglePassword.setOnClickListener {
            isCurrentPasswordVisible = !isCurrentPasswordVisible
            isNewPasswordVisible = !isNewPasswordVisible
            isConfirmNewPasswordVisible = !isConfirmNewPasswordVisible

            togglePasswordVisibility(
                etCurrentPassword,
                isCurrentPasswordVisible
            )
            togglePasswordVisibility(
                etNewPassword,
                isNewPasswordVisible
            )
            togglePasswordVisibility(
                etConfirmNewPassword,
                isConfirmNewPasswordVisible
            )
        }

        return view
    }

    private fun togglePasswordVisibility(editText: EditText, isVisible: Boolean) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        editText.setSelection(editText.text.length) // Mantiene el cursor al final del texto
    }

    override fun showSuccess(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun traernombre(perfil: PerfilUsuarioResponse) {
        // No aplica en este fragmento
    }

    override fun mostrarBotonGestionarEquipo() {
        // No aplica en este fragmento
    }

    override fun mostrarBotonCrearEquipo() {
        TODO("Not yet implemented")
    }

    override fun mostrarMensajeSnackBar(message: String) {
        TODO("Not yet implemented")
    }

    override fun showValidacionInscripcion(success: Boolean, equipo: Equipo?) {
        // No aplica en este fragmento
    }

    override fun showInscripcionError(message: String) {
        // No aplica en este fragmento
    }

    override fun navigateToCrearEquipo() {
        TODO("Not yet implemented")
    }

    override fun navigateToGestionarEquipo(equipo: Equipo) {
        TODO("Not yet implemented")
    }

    override fun showCampeonatos(campeonatos: List<Campeonatos>) {
        // No aplica en este fragmento
    }

    override fun showLoading() {
        // Muestra un indicador de carga si es necesario
    }

    override fun hideLoading() {
        // Oculta el indicador de carga si es necesario
    }
}

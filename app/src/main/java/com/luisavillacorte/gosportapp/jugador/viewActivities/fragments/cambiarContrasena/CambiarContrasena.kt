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
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.NuevaContrasenaRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
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

        // Inicializar el presenter
        val apiService = RetrofitInstance.createService(HomeApiService::class.java)
        presenter = HomeCampeonatosPresenter(this, requireContext(), apiService)

        // Manejar el evento de clic para guardar los cambios de contraseña
        btnGuardarCambios.setOnClickListener {
            val nuevaContrasena = etNewPassword.text.toString()
            val confirmarContrasena = etConfirmNewPassword.text.toString()

            if (nuevaContrasena == confirmarContrasena) {
                val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("user_id", null)

                Log.d("CambiarContrasena", "User ID recuperado: $userId")

                if (userId != null) {
                    val nuevaContrasenaRequest = NuevaContrasenaRequest(nuevaContrasena)
                    presenter.cambiarContrasena(nuevaContrasenaRequest) // Llama al método del presenter
                } else {
                    showError("User ID no disponible")
                }
            } else {
                showError("Las contraseñas no coinciden")
            }
        }

        return view
    }

    // Método para alternar la visibilidad de la contraseña
    private fun togglePasswordVisibility(editText: EditText, isPasswordVisible: Boolean): Boolean {
        if (isPasswordVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        }
        editText.setSelection(editText.text.length)
        return !isPasswordVisible
    }

    // Métodos de la interfaz HomeCampeonatosContract.View

    override fun showSuccess(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        // Implementar lógica para mostrar un indicador de carga si es necesario
    }

    override fun hideLoading() {
        // Implementar lógica para ocultar el indicador de carga si es necesario
    }

    override fun traernombre(perfil: PerfilUsuarioResponse) {
        // Este método no se usará en este fragmento, pero debe ser implementado
    }

    override fun showCampeonatos(campeonatos: List<Campeonatos>) {
        // Este método no se usará en este fragmento, pero debe ser implementado
    }
}

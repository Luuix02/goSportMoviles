package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.editarPerfil

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager

class Editarperfil : Fragment(), HomeCampeonatosContract.View {

    private lateinit var presenter: HomeCampeonatosPresenter
    private lateinit var nombreTextView: TextView
    private lateinit var identificacio: TextView
    private lateinit var fichaperfil: TextView
    private lateinit var telefono: TextView
    private lateinit var jornada: TextView
    private lateinit var correo: TextView
    private lateinit var btnGuardarCambios: Button

    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editarperfil, container, false)

        nombreTextView = view.findViewById(R.id.edtNombre)
        identificacio = view.findViewById(R.id.edtidentificacionperfil)
        fichaperfil = view.findViewById(R.id.fichaperfil)
        telefono = view.findViewById(R.id.edtTelefono)
        jornada = view.findViewById(R.id.edtjornadaperfil)
        correo = view.findViewById(R.id.edtEmail)
        btnGuardarCambios = view.findViewById(R.id.btnGuardar)

        // Obtener el ID del usuario desde SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("user_id", null)
        Log.d("Editarperfil", "User ID recuperado: $userId")

        val apiService = RetrofitInstance.createService(HomeApiService::class.java)
        presenter = HomeCampeonatosPresenter(this, requireContext(), apiService)

        if (userId == null) {
            Log.d("Editarperfil", "User ID es nulo, llamando a obtenerPerfilUsuario")
            presenter.getPerfilUsuario()
        } else {
            Log.d("Editarperfil", "User ID ya disponible: $userId")
            // Cargar el perfil si el userId está disponible
            presenter.getPerfilUsuario()
        }

        btnGuardarCambios.setOnClickListener {
            if (userId == null) {
                showError("User ID no disponible")
                return@setOnClickListener
            }

            val perfilActualizado = PerfilUsuarioResponse(
                id = userId!!,
                nombres = nombreTextView.text.toString(),
                telefono = telefono.text.toString(),
                correo = correo.text.toString(),
                ficha = fichaperfil.text.toString(),
                jornada = jornada.text.toString(),
                identificacion = identificacio.text.toString(),
                genero = "femenino",
                programa = "aseador"
            )
            presenter.actualizarPerfilUsuario(perfilActualizado)
        }

        return view
    }

    override fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse) {
        nombreTextView.text = perfilUsuarioResponse.nombres
        identificacio.text = perfilUsuarioResponse.identificacion
        fichaperfil.text = perfilUsuarioResponse.ficha
        telefono.text = perfilUsuarioResponse.telefono
        jornada.text = perfilUsuarioResponse.jornada
        correo.text = perfilUsuarioResponse.correo

        // Guardar el ID del usuario en SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_id", perfilUsuarioResponse.id)
        editor.apply()
        Log.d("Editarperfil", "User ID guardado: ${perfilUsuarioResponse.id}")

        // Actualizar el userId local
        userId = perfilUsuarioResponse.id
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        Log.d("Editarperfil", "Error: $message")
    }

    override fun showLoading() {
        // Mostrar indicador de carga
    }

    override fun hideLoading() {
        // Ocultar indicador de carga
    }

    override fun showCampeonatos(campeonatos: List<Campeonatos>) {
        // Lógica para mostrar campeonatos si es necesario
    }
}

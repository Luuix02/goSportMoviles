package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.perfilFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.cambiarContrasena.CambiarContrasena
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.editarPerfil.Editarperfil

class FragmentPerfil : Fragment(), HomeCampeonatosContract.View {

    private lateinit var presenter: HomeCampeonatosPresenter
    private lateinit var nombreTextView: TextView
    private lateinit var telefonoTextView: TextView
    private lateinit var editarperfil: TextView
    private lateinit var cambiarcontrasena: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        nombreTextView = view.findViewById(R.id.nombre_text_view)
        telefonoTextView = view.findViewById(R.id.telefono_text_view)
        editarperfil = view.findViewById(R.id.editar_perfil)
        cambiarcontrasena = view.findViewById(R.id.link_cambiar_contrasena)

        cambiarcontrasena.setOnClickListener {
            val cambiarfragment = CambiarContrasena()
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.fragment_container, cambiarfragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        editarperfil.setOnClickListener {
            val editarPerfilFragment = Editarperfil()
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            // Reemplaza el fragmento actual con el nuevo
            fragmentTransaction.replace(R.id.fragment_container, editarPerfilFragment)
            fragmentTransaction.addToBackStack(null) // Permite volver atrás con el botón de retroceso
            fragmentTransaction.commit()
        }

        val apiService = RetrofitInstance.createService(HomeApiService::class.java)
        val tokenManager = TokenManager(requireContext())

        presenter = HomeCampeonatosPresenter(this, requireContext(), apiService)
        presenter.getPerfilUsuario()

        return view
    }

    override fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse) {
        Log.d("FragmentPerfil", "Perfil recibido: ${perfilUsuarioResponse.nombres}, Tel: ${perfilUsuarioResponse.telefono}")
        nombreTextView.text = perfilUsuarioResponse.nombres
        telefonoTextView.text = "Tel: ${perfilUsuarioResponse.telefono}"
    }

    override fun showSuccess(message: String) {
        // Aquí solo se mostraría un mensaje de éxito, si implementas la lógica
    }

    override fun showError(message: String) {
        Log.e("FragmentPerfil", "Error: $message")
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        // Implementar la lógica para mostrar un indicador de carga si es necesario
    }

    override fun hideLoading() {
        // Implementar la lógica para ocultar el indicador de carga si es necesario
    }

    override fun showCampeonatos(campeonatos: List<Campeonatos>) {
        // Implementar la lógica para mostrar campeonatos si es necesario
    }

    override fun navigateToCrearEquipo() {
        // Navegar a la creación de equipo
    }

    override fun showInscripcionError(message: String) {
        // Mostrar un error relacionado con la inscripción
    }

    override fun navigateToGestionarEquipo(equipo: Equipo) {
        // Navegar a la gestión del equipo
    }

    override fun mostrarBotonGestionarEquipo() {
        // Mostrar botón para gestionar equipo
    }

    override fun mostrarMensajeSnackBar(message: String) {
        // Mostrar un Snackbar
    }

    override fun mostrarBotonCrearEquipo() {
        // Mostrar botón para crear equipo
    }

    override fun showValidacionInscripcion(estaInscrito: Boolean, equipo: Equipo?) {
        // Validar la inscripción del usuario y mostrar si está inscrito
    }
}

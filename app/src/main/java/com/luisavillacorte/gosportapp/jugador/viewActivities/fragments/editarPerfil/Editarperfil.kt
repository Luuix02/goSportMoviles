package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.editarPerfil

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
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager

class Editarperfil : Fragment(), HomeCampeonatosContract.View {

    private lateinit var presenter: HomeCampeonatosPresenter
    private lateinit var nombreTextView: TextView
    private lateinit var identificacio: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_editarperfil, container, false)

        nombreTextView = view.findViewById(R.id.edtNombre)
        identificacio = view.findViewById(R.id.edtidentificacionperfil)

        val apiService = RetrofitInstance.createService(HomeApiService::class.java)
        val tokenManager = TokenManager(requireContext())
        presenter = HomeCampeonatosPresenter(this, requireContext(), apiService)
        presenter.getPerfilUsuario()

        return view
    }

    override fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse) {
        Log.d("Framentoeditar", "Perfil recibido: ${perfilUsuarioResponse.nombres}, Tel: ${perfilUsuarioResponse.telefono}")
        nombreTextView.text = perfilUsuarioResponse.nombres
        identificacio.text = "Tel: ${perfilUsuarioResponse.telefono}"
    }

    override fun showError(message: String) {
        Log.e("Framentoeditar", "Error: $message")
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
}

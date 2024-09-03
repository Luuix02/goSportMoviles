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
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos

class FragmentPerfil : Fragment(), HomeCampeonatosContract.View {

    private lateinit var presenter: HomeCampeonatosPresenter
    private lateinit var nombreTextView: TextView
    private lateinit var telefonoTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        nombreTextView = view.findViewById(R.id.nombre_text_view)
        telefonoTextView = view.findViewById(R.id.telefono_text_view)

        val apiService = RetrofitInstance.createService(HomeApiService::class.java)
        val tokenManager = TokenManager(requireContext()) // Obtén el TokenManager

        presenter = HomeCampeonatosPresenter(this, requireContext(), apiService)
        presenter.getPerfilUsuario()

        return view
    }

    override fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse) {
        Log.d("FragmentPerfil", "Perfil recibido: ${perfilUsuarioResponse.nombres}, Tel: ${perfilUsuarioResponse.telefono}")
        nombreTextView.text = perfilUsuarioResponse.nombres
        telefonoTextView.text = "Tel: ${perfilUsuarioResponse.telefono}"
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
}

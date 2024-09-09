package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.homeFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.CampeonatosAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.HomeCampeonatosPresenter

class FragmentHome : Fragment(), HomeCampeonatosContract.View {

    private lateinit var presenter: HomeCampeonatosPresenter
    private lateinit var recyclerViewCampeonatos: RecyclerView
    private lateinit var campeonatosAdapter: CampeonatosAdapter
    private lateinit var nombrejuga: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa las vistas
        recyclerViewCampeonatos = view.findViewById(R.id.recyclerViewCampeonatos)
        nombrejuga = view.findViewById(R.id.nombreusuario)

        // Configura el layout manager para mostrar los items horizontalmente
        recyclerViewCampeonatos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Inicializa el Presenter
        presenter = HomeCampeonatosPresenter(
            this, requireContext(),
            RetrofitInstance.createService(HomeApiService::class.java)
        )

        // Llama a los métodos del Presenter
        presenter.getCampeonatos()
        presenter.getPerfilUsuario()
    }

    override fun showLoading() {
        // Muestra un indicador de carga si es necesario
    }

    override fun hideLoading() {
        // Oculta el indicador de carga si es necesario
    }

    override fun showCampeonatos(campeonatos: List<Campeonatos>) {
        if (campeonatos.isNotEmpty()) {
            campeonatosAdapter = CampeonatosAdapter(campeonatos)
            recyclerViewCampeonatos.adapter = campeonatosAdapter
        } else {
            context?.let {
                Toast.makeText(it, "No hay campeonatos disponibles", Toast.LENGTH_SHORT).show()
            } ?: Log.e("HomeFragment", "Context is null, cannot show toast")
        }
    }

    override fun showError(message: String) {
        context?.let {
            Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
        } ?: Log.e("HomeFragment", "Context is null, cannot show toast")
    }

    override fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse) {
        val nombreJugador = perfilUsuarioResponse.nombres
        nombrejuga.text = "Hola $nombreJugador, Bienvenido a GoSport"
        Log.d("HomeFragment", "Nombre del usuario: $nombreJugador")
    }

    override fun showSuccess(message: String) {
        TODO("Not yet implemented")
    }
}

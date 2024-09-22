package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.CampeonatoIntercentro

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.campIntercentros.CampeonatInter
import com.luisavillacorte.gosportapp.jugador.adapters.model.campIntercentros.CampeonatintContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.campIntercentros.CampeonatoInterPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.model.campIntercentros.InterCampAdapter

class IntercentrosCampoenato : Fragment(), CampeonatintContract.View {

    private lateinit var presenter: CampeonatoInterPresenter
    private lateinit var recyclerViewCampeonatos: RecyclerView
    private lateinit var campeonatosAdapter: InterCampAdapter
    private lateinit var progressBar: ProgressBar  // ProgressBar para mostrar el loading

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intercentros_campoenato, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CampeonatoInterPresenter(
            this,
            RetrofitInstance.createService(HomeApiService::class.java)
        )

        recyclerViewCampeonatos = view.findViewById(R.id.recyclerViewCampeonatosintercentros)
        progressBar = view.findViewById(R.id.progressBarintercentros)

        recyclerViewCampeonatos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        presenter.obtenerCampeonatos()
    }

    override fun mostrarCampeonatos(campeonatos: List<CampeonatInter>) {
        if (campeonatos.isNotEmpty()) {
            campeonatosAdapter = InterCampAdapter(campeonatos, presenter)
            recyclerViewCampeonatos.adapter = campeonatosAdapter


            campeonatosAdapter.filterByIntercentros()
        } else {
            context?.let {
                Toast.makeText(it, "No hay campeonatos disponibles", Toast.LENGTH_SHORT).show()
            } ?: Log.e("HomeFragment", "Context is null, cannot show toast")
        }
    }


    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
    }

    override fun showError(message: String) {
        hideLoading()
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showSuccess(message: String) {
        hideLoading()  // Oculta el loading al mostrar el mensaje de éxito
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.misPartidosFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.misPartidosService.MisPartidosApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.miEquipo.VerJugadoresAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos.MisPartidosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos.MisPartidosPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos.ResultadosDialogFragment
import com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos.ResultadosResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos.VsAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos.VsResponse


class FragmentMisPartidos : Fragment(), MisPartidosContract.View {

    private lateinit var presenter: MisPartidosPresenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_mis_partidos, container, false)

        // Configurar RecyclerView y Adapter
        recyclerView = view.findViewById(R.id.recyclerVsMisPartidos)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = VsAdapter {
            idVs ->
            val sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()){
                putString("idVs", idVs)
                apply()
            }
            presenter.cargarResultados(idVs)
        }
        recyclerView.adapter = adapter

        // Inicializar el Presenter
        presenter = MisPartidosPresenter(
            this,
            RetrofitInstance.createService(MisPartidosApiService::class.java), // Corrección aquí
            requireContext()
        )

        // Fetch data
        presenter.obtenerVsEquipo()

        return view
    }

    override fun mostrarVs(vsList: List<VsResponse>) {
        adapter.submitList(vsList)
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    override fun mostrarResultados(resultados: ResultadosResponse){
        val dialog = ResultadosDialogFragment.newInstance(resultados)
        dialog.show(childFragmentManager, "resultadosDialog")

    }
}

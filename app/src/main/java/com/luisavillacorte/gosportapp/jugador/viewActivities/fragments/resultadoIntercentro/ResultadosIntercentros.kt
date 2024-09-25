package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.resultadoIntercentro

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter.Resulatdos
import com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter.ResultadosAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter.ResultadosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter.ResultadosModel
import com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter.ResultadosPresenter

class ResultadosIntercentros : Fragment(), ResultadosContract.View {
    private lateinit var presenter: ResultadosContract.Presenter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResultadosAdapter
    private val TAG = "ResultadosIntercentros"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resultados_intercentros, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewResultados)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Recuperar el ID del campeonato y los IDs de los equipos de SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val campeonatoId = sharedPreferences.getString("CAMPEONATO_ID", null)
        val equipoLocalId = sharedPreferences.getString("EQUIPO_LOCAL_ID", null)
        val equipoVisitanteId = sharedPreferences.getString("EQUIPO_VISITANTE_ID", null)

        // Instancia del presenter
        presenter = ResultadosPresenter(this, ResultadosModel())

        // Verificar si el ID del campeonato está disponible
        if (campeonatoId != null) {
            Log.d(TAG, "Obteniendo resultados para el campeonato ID: $campeonatoId")
            presenter.getResultados(campeonatoId) // Pasar el ID del campeonato a getResultados
        } else {
            showError("ID del campeonato no disponible")
        }

        return view
    }

    override fun showResultados(resultados: List<Resulatdos>) {
        Log.d(TAG, "Mostrando resultados: $resultados")
        adapter = ResultadosAdapter(resultados)
        recyclerView.adapter = adapter
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

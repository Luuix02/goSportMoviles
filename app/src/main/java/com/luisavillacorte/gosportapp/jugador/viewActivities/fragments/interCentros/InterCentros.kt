package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.interCentros

import InterCentrosPresenter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.InterCentrosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.InterCentrosModel
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.Partidos
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.PartidosAdapter

class InterCentros : Fragment(), InterCentrosContract.View {

    private lateinit var presenter: InterCentrosPresenter
    private lateinit var recyclerView: RecyclerView
    private val partidosAdapter = PartidosAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inter_centros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewPartidosJugados)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = partidosAdapter

        presenter = InterCentrosPresenter(this, InterCentrosModel(), requireContext())

        val equipoId = obtenerEquipoId()
        if (equipoId != null) {
            presenter.loadPartidos(equipoId)
        } else {
            showError("No se encontró el ID del equipo en las preferencias.")
        }
    }

    private fun obtenerEquipoId(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("EQUIPO_ID", null)
    }

    override fun showPartidos(partidos: List<Partidos>) {
        partidosAdapter.setPartidos(partidos)
    }

    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

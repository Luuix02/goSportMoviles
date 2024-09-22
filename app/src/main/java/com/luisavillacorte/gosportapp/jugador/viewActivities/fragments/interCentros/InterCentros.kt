package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.interCentros

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
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscritoData
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.*

class InterCentros : Fragment(), InterCentrosContract.View {

    private lateinit var presenter: InterCentrosPresenter
    private lateinit var recyclerViewPartidos: RecyclerView
    private lateinit var recyclerViewEquipos: RecyclerView
    private lateinit var recyclerViewPosiciones: RecyclerView
    private val partidosAdapter = PartidosAdapter()
    private val equiposAdapter = EquiposAdapter()
    private val posicionesAdapter = PosicionesAdapter()

    companion object {
        fun newInstance(): InterCentros {
            return InterCentros()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inter_centros, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewPartidos = view.findViewById(R.id.recyclerViewPartidosJugados)
        recyclerViewPartidos.layoutManager = LinearLayoutManager(context)
        recyclerViewPartidos.adapter = partidosAdapter

        recyclerViewEquipos = view.findViewById(R.id.recyclerViewEquiposInscritos)
        recyclerViewEquipos.layoutManager = LinearLayoutManager(context)
        recyclerViewEquipos.adapter = equiposAdapter

        recyclerViewPosiciones = view.findViewById(R.id.recyclerViewPosiciones)
        recyclerViewPosiciones.layoutManager = LinearLayoutManager(context)
        recyclerViewPosiciones.adapter = posicionesAdapter

        presenter = InterCentrosPresenter(this, InterCentrosModel(), requireContext())

        val equipoId = obtenerEquipoId()
        if (equipoId != null) {
            presenter.loadPartidos(equipoId)
        } else {
            showError("No se encontró el ID del equipo en las preferencias.")
        }

        // Cargar equipos inscritos y posiciones usando el ID del campeonato guardado en SharedPreferences
        val idCampeonato = obtenerIdCampeonato()
        if (idCampeonato != null) {
            presenter.loadEquiposInscritos(idCampeonato)
            presenter.loadTablaPosiciones(idCampeonato)
        } else {
            showError("No se encontró el ID del campeonato en las preferencias.")
        }
    }

    // Obtener el ID del campeonato desde SharedPreferences
    private fun obtenerIdCampeonato(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("CAMPEONATO_ID", null)
    }

    // Obtener el ID del equipo desde SharedPreferences
    private fun obtenerEquipoId(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("EQUIPO_ID", null)
    }

    // Mostrar los partidos en el RecyclerView
    override fun showPartidos(partidos: List<Partidos>) {
        partidosAdapter.setPartidos(partidos)
    }

    // Mostrar equipos inscritos en el RecyclerView
    override fun showEquiposInscritos(equipos: List<EquipoInscritoData>) {
        equiposAdapter.setEquipos(equipos)
    }

    // Mostrar tabla de posiciones en el RecyclerView
    override fun showTablaPosiciones(posiciones: List<PosicionEquipoData>) {
        posicionesAdapter.setPosiciones(posiciones)
    }

    // Mostrar mensajes de error
    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

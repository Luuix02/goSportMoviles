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
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.EquiposAdapter
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.InterCentrosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.InterCentrosModel
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.Partidos
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.PartidosAdapter

class InterCentros : Fragment(), InterCentrosContract.View {

    private lateinit var presenter: InterCentrosPresenter
    private lateinit var recyclerViewPartidos: RecyclerView
    private lateinit var recyclerViewEquipos: RecyclerView
    private val partidosAdapter = PartidosAdapter()
    private val equiposAdapter = EquiposAdapter() // Asume que ya creaste EquiposAdapter

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

        presenter = InterCentrosPresenter(this, InterCentrosModel(), requireContext())

        // Cargar los partidos jugados
        val equipoId = obtenerEquipoId()
        if (equipoId != null) {
            presenter.loadPartidos(equipoId)
        } else {
            showError("No se encontró el ID del equipo en las preferencias.")
        }

        // Cargar los equipos inscritos en el campeonato con un ID fijo
        val idCampeonatoFijo = "66ead0e8666c8424efc8c90b" // Reemplaza esto con un ID válido para la prueba
        presenter.loadEquiposInscritos(idCampeonatoFijo)
    }

    // Método para obtener el ID del equipo desde SharedPreferences
    private fun obtenerEquipoId(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("EQUIPO_ID", null)
    }

    // Método para obtener el ID del campeonato desde SharedPreferences
    private fun obtenerIdCampeonato(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("CAMPEONATO_ID", null)
    }

    // Mostrar los partidos en el RecyclerView
    override fun showPartidos(partidos: List<Partidos>) {
        partidosAdapter.setPartidos(partidos)
    }

    // Mostrar los equipos inscritos en el RecyclerView
    override fun showEquiposInscritos(equipos: List<EquipoInscritoData>) {
        equiposAdapter.setEquipos(equipos)
    }

    // Mostrar error en caso de fallos
    override fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

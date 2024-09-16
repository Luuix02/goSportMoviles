package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.interCentros

import InterCentrosPresenter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        presenter = InterCentrosPresenter(this, InterCentrosModel())

        presenter.loadPartidos("66e51f36c847dc29b9c65fb3")
    }

    override fun showPartidos(partidos: List<Partidos>) {
        partidosAdapter.setPartidos(partidos)
    }

    override fun showError(message: String) {
    }
}

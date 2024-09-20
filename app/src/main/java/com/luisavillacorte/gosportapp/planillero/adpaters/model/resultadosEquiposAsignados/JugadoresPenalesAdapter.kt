package com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Participantes

class JugadoresPenalesAdapter(private val  jugadoresPenalesList: List<Participantes>): RecyclerView.Adapter<JugadoresPenalesAdapter.PenalHover>() {
    inner class PenalHover (view:View): RecyclerView.ViewHolder(view){
        val dorsalTextView: TextView = view.findViewById(R.id.dorsalParticipantePenales)
        val nombreTextView: TextView = view.findViewById(R.id.nombresJugadoresPenales)
        fun bind(penal: Participantes) {
            dorsalTextView.text = penal.dorsal.toString()
            nombreTextView.text = penal.nombres

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenalHover {
        val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_penales_jugadores_desing, parent, false)
        return PenalHover(view)
    }

    override fun getItemCount(): Int {
        return jugadoresPenalesList.size;
    }

    override fun onBindViewHolder(holder: PenalHover, position: Int) {
        val penal = jugadoresPenalesList[position]
        holder.bind(penal)
    }
}
package com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Participante

class AmarillasAdapter(private val context: Context,
                       private val participantes: List<Participante>
) : RecyclerView.Adapter<AmarillasAdapter.AmarillaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmarillaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ver_goles_en_ver_resultados, parent, false)
        return AmarillaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmarillaViewHolder, position: Int) {
        val participante = participantes[position]
        holder.bind(participante)
    }

    override fun getItemCount(): Int {
        return participantes.size
    }

    class AmarillaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nombreJugadorTextView: TextView = itemView.findViewById(R.id.NombreGoleadorVerResultados)
        private val Dorsal: TextView = itemView.findViewById(R.id.DorsalGoleadorVerResultados)
        private val Ficha: TextView = itemView.findViewById(R.id.FichaVerResultados)
        private val cantidadFaltasTextView: TextView = itemView.findViewById(R.id.NumeroDeCometidas)

        fun bind(participante: Participante) {
            nombreJugadorTextView.text = participante.nombreJugador
            Dorsal.text = "${participante.dorsal}"
            Ficha.text = "${participante.ficha}"
//            cantidadFaltasTextView.text = "${participante.cantidadFaltas}"
        }
    }
}

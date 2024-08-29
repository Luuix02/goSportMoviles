package com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Participante

class RojasAdapter(private val items: List<Participante>) : RecyclerView.Adapter<RojasAdapter.RojasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RojasViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ver_goles_en_ver_resultados, parent, false)
        return RojasViewHolder(view)
    }

    override fun onBindViewHolder(holder: RojasViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class RojasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Participante) {
            itemView.findViewById<TextView>(R.id.DorsalGoleadorVerResultados).text = item.dorsal.toString()
            itemView.findViewById<TextView>(R.id.NombreGoleadorVerResultados).text = item.nombreJugador
            itemView.findViewById<TextView>(R.id.FichaVerResultados).text = item.ficha.toString()
        }
    }
}
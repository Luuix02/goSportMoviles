package com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Participante

class GolesAdapter (private val items: List<Participante>) : RecyclerView.Adapter<GolesAdapter.GolesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GolesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ver_goles_en_ver_resultados, parent, false)
        return GolesViewHolder(view)
    }

    override fun onBindViewHolder(holder: GolesViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class GolesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Participante) {
            itemView.findViewById<TextView>(R.id.DorsalGoleadorVerResultados).text = item.dorsal.toString()
            itemView.findViewById<TextView>(R.id.NombreGoleadorVerResultados).text = item.nombres
            itemView.findViewById<TextView>(R.id.FichaVerResultados).text = item.ficha.toString()
            itemView.findViewById<TextView>(R.id.NumeroDeCometidas).text = item.totalGoles.toString()
        }
    }
}
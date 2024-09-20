package com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R

class PosicionesAdapter : RecyclerView.Adapter<PosicionesAdapter.PosicionViewHolder>() {
    private var posiciones: List<PosicionEquipoData> = emptyList()

    inner class PosicionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombreEquipo)
        //private val textViewPuntos: TextView = itemView.findViewById(R.id.puntosview)

        fun bind(posicion: PosicionEquipoData) {
            textViewNombre.text = posicion.equipo.nombreEquipo
            //textViewPuntos.text = posicion.puntos.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosicionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipo, parent, false)
        return PosicionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PosicionViewHolder, position: Int) {
        holder.bind(posiciones[position])
    }

    override fun getItemCount(): Int = posiciones.size

    fun setPosiciones(newPosiciones: List<PosicionEquipoData>) {
        posiciones = newPosiciones
        notifyDataSetChanged()
    }
}

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
        private val textViewPosicion: TextView = itemView.findViewById(R.id.textViewPosicion)
        private val textViewNombre: TextView = itemView.findViewById(R.id.textViewNombreEquipo)
        private val textViewPuntos: TextView = itemView.findViewById(R.id.textViewpuntos)

        // Ahora bind también acepta el índice para la posición
        fun bind(posicion: PosicionEquipoData, index: Int) {
            textViewPosicion.text = (index + 1).toString() // Asignar la posición
            textViewNombre.text = posicion.equipo.nombreEquipo
            textViewPuntos.text = posicion.pts.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosicionViewHolder {
        // Asegúrate de usar el layout correcto
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipo, parent, false)
        return PosicionViewHolder(view)
    }

    override fun onBindViewHolder(holder: PosicionViewHolder, position: Int) {
        holder.bind(posiciones[position], position)
    }

    override fun getItemCount(): Int = posiciones.size

    fun setPosiciones(newPosiciones: List<PosicionEquipoData>) {

        posiciones = newPosiciones.sortedByDescending { it.pts }
        notifyDataSetChanged()
    }
}

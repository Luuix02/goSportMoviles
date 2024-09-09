package com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Participante
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Resultados

class TarjetaGeneral(private val items: Resultados,
                     private val participantesAmarillas: List<Participante>,
                     private val participantesRojas: List<Participante>
) : RecyclerView.Adapter<TarjetaGeneral.CardViewHolder>() {

    companion object {
        const val Goles = 0
        const val Amarillas = 1
        const val Rojas = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> Goles
            1 -> Amarillas
            2 -> Rojas
            else -> throw IllegalArgumentException("Posición desconocida")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tarjeta_general, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        when (getItemViewType(position)) {
            Goles -> {
                val goles = items.equipo1.goles.jugadorGoleador
                if (goles.isEmpty()) {
                    holder.showNoDataMessage("Goles","No hay goles")
                } else {
                    holder.bind("Goles", goles)
                }
            }

            Amarillas -> {
                if (participantesAmarillas.isEmpty()) {
                    holder.showNoDataMessage("Tarjetas Amarillas","No hay tarjetas amarillas")
                } else {
                    holder.bind("Tarjetas Amarillas", participantesAmarillas)
                }
            }

            Rojas -> {
                if (participantesRojas.isEmpty()) {
                    holder.showNoDataMessage("Tarjetas Rojas","No hay tarjetas rojas")
                } else {
                    holder.bind("Tarjetas Rojas", participantesRojas)
                }
            }
        }
    }
    override fun getItemCount(): Int {
        return 3
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardTitle: TextView = itemView.findViewById(R.id.CardGeneralSegunResultados)
        private val recyclerView: RecyclerView = itemView.findViewById(R.id.ParticipantesConResultados)
        private val noDataMessage: TextView = itemView.findViewById(R.id.NoDataMessage) // Añadir un TextView en tu layout



        fun <T> bind(title: String, items: List<T>) {
            cardTitle.text = title
            noDataMessage.visibility = View.GONE

            val adapter = when (title) {
                "Tarjetas Amarillas" -> AmarillasAdapter(itemView.context, items as List<Participante>)
                "Tarjetas Rojas" -> RojasAdapter(items as List<Participante>)
                "Goles" -> GolesAdapter(items as List<Participante>)
                else -> throw IllegalArgumentException("Tipo de datos desconocido")
            }

            recyclerView.layoutManager = LinearLayoutManager(itemView.context)
            recyclerView.adapter = adapter
        }

        fun showNoDataMessage(titulo:String,messsage: String) {
            cardTitle.text = titulo
            recyclerView.visibility = View.GONE // Ocultar el RecyclerView
            noDataMessage.visibility = View.VISIBLE // Mostrar el mensaje de "No hay"
            noDataMessage.text = messsage
        }
    }

}

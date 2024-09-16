package com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luisavillacorte.gosportapp.R

class PartidosAdapter : RecyclerView.Adapter<PartidosAdapter.ViewHolder>() {

    private var partidos: List<Partidos> = emptyList()

    fun setPartidos(partidos: List<Partidos>) {
        this.partidos = partidos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.intercentro, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val partido = partidos[position]
        holder.bind(partido)
    }

    override fun getItemCount(): Int = partidos.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivEscudoLocal: ImageView = itemView.findViewById(R.id.ivEscudoLocal)
        private val tvEquipoLocal: TextView = itemView.findViewById(R.id.tvEquipoLocal)
        private val ivEscudoVisitante: ImageView = itemView.findViewById(R.id.ivEscudoVisitante)
        private val tvEquipoVisitante: TextView = itemView.findViewById(R.id.tvEquipoVisitante)
        private val tvResultadoLocal: TextView = itemView.findViewById(R.id.tvResultadoLocal)
        private val tvResultadoVisitante: TextView = itemView.findViewById(R.id.tvResultadoVisitante)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)

        fun bind(partido: Partidos) {
            val equipoLocal = partido.equipo1
            if (equipoLocal != null) {
                tvEquipoLocal.text = equipoLocal.nombreEquipo ?: "Nombre no disponible"
                Glide.with(itemView.context)
                    .load(equipoLocal.imgLogo)
                    .placeholder(R.drawable.ic_perfil)
                    .error(R.drawable.borar)
                    .into(ivEscudoLocal)
            } else {
                tvEquipoLocal.text = "Sin equipo"
                ivEscudoLocal.setImageResource(R.drawable.ic_perfil)
            }

            val equipoVisitante = partido.equipo2
            if (equipoVisitante != null) {
                tvEquipoVisitante.text = equipoVisitante.nombreEquipo ?: "Nombre no disponible"
                Glide.with(itemView.context)
                    .load(equipoVisitante.imgLogo)
                    .placeholder(R.drawable.ic_perfil)
                    .error(R.drawable.borar)
                    .into(ivEscudoVisitante)
            } else {
                tvEquipoVisitante.text = "Sin equipo"
                ivEscudoVisitante.setImageResource(R.drawable.ic_perfil)
            }

            tvResultadoLocal.text = partido.estado?.toString() ?: "No disponible"
            tvResultadoVisitante.text = partido.estado?.toString() ?: "No disponible"
            tvFecha.text = partido.fecha ?: "Fecha no disponible"

            // Puedes manejar el estado aquí si es necesario
            tvEstado.text = partido.estado?.toString() ?: "Estado no disponible"
        }
    }
}

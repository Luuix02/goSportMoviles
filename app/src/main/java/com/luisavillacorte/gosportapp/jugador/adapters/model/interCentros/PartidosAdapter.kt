package com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivEscudoLocal: ImageView = itemView.findViewById(R.id.ivEscudoLocal)
        private val tvEquipoLocal: TextView = itemView.findViewById(R.id.tvEquipoLocal)
        private val ivEscudoVisitante: ImageView = itemView.findViewById(R.id.ivEscudoVisitante)
        private val tvEquipoVisitante: TextView = itemView.findViewById(R.id.tvEquipoVisitante)
        private val tvResultadoLocal: TextView = itemView.findViewById(R.id.tvResultadoLocal)
        private val tvResultadoVisitante: TextView = itemView.findViewById(R.id.tvResultadoVisitante)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)

        fun bind(partido: Partidos) {
            tvEquipoLocal.text = partido.equipoLocal
            tvEquipoVisitante.text = partido.equipoVisitante
            tvResultadoLocal.text = partido.resultadoLocal.toString()
            tvResultadoVisitante.text = partido.resultadoVisitante.toString()
            tvFecha.text = partido.fecha

        }
    }
}

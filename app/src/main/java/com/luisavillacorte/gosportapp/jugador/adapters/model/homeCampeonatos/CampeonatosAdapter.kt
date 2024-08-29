package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R

class CampeonatosAdapter(private val campeonatos: List<Campeonatos>) :
    RecyclerView.Adapter<CampeonatosAdapter.CampeonatoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampeonatoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_campeonato, parent, false)
        return CampeonatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CampeonatoViewHolder, position: Int) {
        val campeonato = campeonatos[position]
        holder.bind(campeonato)
    }

    override fun getItemCount(): Int = campeonatos.size

    inner class CampeonatoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreCampeonato: TextView = itemView.findViewById(R.id.nombreCampeonato)
        private val descripcion: TextView = itemView.findViewById(R.id.descripcion)
        private val btnVerDetalles: Button = itemView.findViewById(R.id.btnVerDetalles)
        private val btnincribir: TextView = itemView.findViewById(R.id.btnincribirme)
        private  val estadocampeonato: TextView=itemView.findViewById(R.id.estado)
        private val categoria:TextView=itemView.findViewById(R.id.categoria)
        private val fechaini:TextView=itemView.findViewById(R.id.fechaInicio)
        private val fechafinal:TextView=itemView.findViewById(R.id.fechaFin)



        fun bind(campeonato: Campeonatos) {
            nombreCampeonato.text = campeonato.nombreCampeonato
            descripcion.text = campeonato.descripcion
            estadocampeonato.text=campeonato.estadoCampeonato
            categoria.text=campeonato.nombreDisciplinas
            fechaini.text=campeonato.fechaInicio
            fechafinal.text=campeonato.fechaFin

            // Verificar el estado del campeonato
            when (campeonato.estadoCampeonato) {
                "Ejecucion" -> {
                    btnVerDetalles.visibility = View.VISIBLE
                    btnincribir.visibility = View.GONE
                    // Configurar el botón para llevar a otra actividad
                    btnVerDetalles.setOnClickListener {
                        // Código para navegar a la página de detalles
                    }
                }
                "Inscripcion" -> {
                    btnincribir.visibility = View.VISIBLE
                    btnVerDetalles.visibility = View.GONE
                }
                else -> {
                    // Si hay otros estados, ocultar ambos
                    btnVerDetalles.visibility = View.GONE
                    btnincribir.visibility = View.GONE
                }
            }
        }
    }
}

package com.luisavillacorte.gosportapp.jugador.adapters.model.campIntercentros

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R

class InterCampAdapter(
    private var campeonatos: List<CampeonatInter>,
    private val presenter: CampeonatintContract.Presenter
) : RecyclerView.Adapter<InterCampAdapter.CampeViewHolder>() {

    private var allCampeonatos: List<CampeonatInter> = campeonatos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_intercentros, parent, false)
        return CampeViewHolder(view)
    }
    fun filterByIntercentros() {
        campeonatos = allCampeonatos.filter { it.tipoCampeonato == "Intercentros" } // Asegúrate de que `tipo` sea la propiedad correcta
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: CampeViewHolder, position: Int) {
        val campeonato = campeonatos[position]
        holder.bind(campeonato)
    }

    override fun getItemCount(): Int = campeonatos.size

    inner class CampeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreCampeonato: TextView = itemView.findViewById(R.id.nombreCampeonat)
        private val descripcion: TextView = itemView.findViewById(R.id.descripcio)
        private val estadoCampeonato: TextView = itemView.findViewById(R.id.estad)
        private val categoria: TextView = itemView.findViewById(R.id.categori)
        private val fechaIni: TextView = itemView.findViewById(R.id.fechaInici)
        private val fechaFin: TextView = itemView.findViewById(R.id.fechaFi)
        private val tipocampeonato: TextView = itemView.findViewById(R.id.tipocampeonat)
        private val botonverdeta :TextView=itemView.findViewById(R.id.btnintercnetrosverdetalles)

        fun bind(campeonato: CampeonatInter) {
            nombreCampeonato.text = campeonato.nombreCampeonato
            descripcion.text = campeonato.descripcion
            estadoCampeonato.text = campeonato.estadoCampeonato
            categoria.text = campeonato.nombreDisciplinas
            fechaIni.text = campeonato.fechaInicio
            fechaFin.text = campeonato.fechaFin
            tipocampeonato.text=campeonato.tipoCampeonato
        }
    }
}

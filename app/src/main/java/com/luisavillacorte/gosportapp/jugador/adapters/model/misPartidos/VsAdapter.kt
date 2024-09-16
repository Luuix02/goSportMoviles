package com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.squareup.picasso.Picasso


class VsAdapter(private val onResultClick: (String) -> Unit) : ListAdapter<VsResponse, VsAdapter.VsViewHolder>(VsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vs_mis_partidos, parent, false)
        return VsViewHolder(view, onResultClick)
    }

    override fun onBindViewHolder(holder: VsViewHolder, position: Int) {
        val vsResponse = getItem(position)
        holder.bind(vsResponse)
    }

    class VsViewHolder(itemView: View, private val onResultClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val fotoLogoEquipo1: ImageView = itemView.findViewById(R.id.LogoEquipo1)
        private val textEquipo1: TextView = itemView.findViewById(R.id.nombreEquipo1)
        private val fotoLogoEquipo2: ImageView = itemView.findViewById(R.id.LogoEquipo2)
        private val textEquipo2: TextView = itemView.findViewById(R.id.nombreEquipo2)
        private val fechaJuego: TextView = itemView.findViewById(R.id.fechaPartido)
        private val botonVerResultados: Button = itemView.findViewById(R.id.verResultadosVs)

        fun bind(vsResponse: VsResponse) {

            Picasso.get().load(vsResponse.equipo1?.informacion?.team1?.Equipo?.imgLogo).into(fotoLogoEquipo1)
            textEquipo1.text = vsResponse.equipo1?.informacion?.team1?.Equipo?.nombreEquipo ?: "Nombre del equipo no disponible"

            Picasso.get().load(vsResponse.equipo2?.informacion?.team2?.Equipo?.imgLogo).into(fotoLogoEquipo2)
            textEquipo2.text = vsResponse.equipo2?.informacion?.team2?.Equipo?.nombreEquipo ?: "Nombre del equipo no disponible"

            fechaJuego.text = "${vsResponse.fecha} - ${vsResponse.hora}"

            botonVerResultados.setOnClickListener{
                vsResponse._id?.let { idVs -> onResultClick(idVs)}
            }
        }
    }

    class VsDiffCallback : DiffUtil.ItemCallback<VsResponse>() {
        override fun areItemsTheSame(oldItem: VsResponse, newItem: VsResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: VsResponse, newItem: VsResponse): Boolean {
            return oldItem == newItem
        }
    }
}

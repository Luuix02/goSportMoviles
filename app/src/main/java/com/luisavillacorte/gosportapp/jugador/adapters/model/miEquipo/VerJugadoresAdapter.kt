package com.luisavillacorte.gosportapp.jugador.adapters.model.miEquipo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.JugadoresAdapter

class VerJugadoresAdapter(

    private val jugadores: List<User>
) : RecyclerView.Adapter<VerJugadoresAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreJugador: TextView = itemView.findViewById(R.id.nombreJugador)
        val fichaJugador: TextView = itemView.findViewById(R.id.fichaJugador)
        val dorsalJugador: TextView = itemView.findViewById(R.id.dorsalJugador)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jugadores_seleccionados_ver_mi_equipo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jugador = jugadores[position]
        holder.nombreJugador.text = jugador.nombres
        holder.fichaJugador.text = jugador.ficha
        holder.dorsalJugador.text = jugador.dorsal ?: ""
    }
    override fun getItemCount() = jugadores.size
}
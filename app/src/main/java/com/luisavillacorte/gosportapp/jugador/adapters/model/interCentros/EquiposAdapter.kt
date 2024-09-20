package com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscritoData

class EquiposAdapter(private var equipos: List<EquipoInscritoData> = listOf()) :
    RecyclerView.Adapter<EquiposAdapter.EquipoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipo, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = equipos[position]
        holder.nombreEquipo.text = equipo.Equipo.nombreEquipo
        //holder.capitan.text = equipo.Equipo.nombreCapitan
        // Configura otros campos si es necesario
    }

    override fun getItemCount() = equipos.size

    fun setEquipos(equipos: List<EquipoInscritoData>) {
        this.equipos = equipos
        notifyDataSetChanged()

        // Agregar un log para mostrar los nombres de los equipos
        val nombresEquipos = equipos.map { it.Equipo.nombreEquipo }
        Log.d("EquiposAdapter", "Equipos actualizados: $nombresEquipos")
    }


    class EquipoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreEquipo: TextView = view.findViewById(R.id.textViewNombreEquipo)
        //val capitan: TextView = view.findViewById(R.id.textViewCapitan)
        // Declara otros TextViews si es necesario
    }
}

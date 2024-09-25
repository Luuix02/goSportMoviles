package com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscritoData

class EquiposAdapter(private var equipos: List<EquipoInscritoData> = listOf()) :
    RecyclerView.Adapter<EquiposAdapter.EquipoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_inscri, parent, false)
        return EquipoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipoViewHolder, position: Int) {
        val equipo = equipos[position]

        // Asigna el nombre del equipo
        holder.nombreEquipo.text = equipo.Equipo.nombreEquipo

        // Usa Glide para cargar la imagen del logo
        Glide.with(holder.itemView.context)
            .load(equipo.Equipo.imgLogo)  // Asegúrate de que imgLogo sea una URL o ruta válida
            .placeholder(R.drawable.avatar)  // Imagen de marcador de posición mientras se carga
            .error(R.drawable.borar)  // Imagen a mostrar si hay un error al cargar
            .into(holder.logos)  // Carga la imagen en el ImageView

        // Puedes configurar otros campos aquí si es necesario
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
        val nombreEquipo: TextView = view.findViewById(R.id.textViewNombreEquipInter)
        val logos: ImageView = view.findViewById(R.id.equipopartiinter)
        // Otros campos si es necesario
    }
}

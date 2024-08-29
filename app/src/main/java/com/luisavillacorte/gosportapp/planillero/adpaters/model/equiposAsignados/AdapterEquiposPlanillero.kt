package com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.squareup.picasso.Picasso

class AdapterEquiposPlanillero (
    private val vsList: List<Vs>,
    private val PasarIdvs: OnItemClickListener
    ) : RecyclerView.Adapter<AdapterEquiposPlanillero.extendEquipos>() {

        interface OnItemClickListener {
            fun onVerResultados(idVs: String)
            fun OnAgregarResultados(idVs: String)
        }

        inner class extendEquipos(view: View) : RecyclerView.ViewHolder(view) {
            val equipo1Name: TextView = view.findViewById(R.id.textEquipo1)
            val equipo2Name: TextView = view.findViewById(R.id.textEquipo2)
            val logoEquipo1: ImageView = view.findViewById(R.id.FotoLogoEquipo1)
            val logoEquipo2: ImageView = view.findViewById(R.id.fotoLogoEquipo2)
            val FechaJuego: TextView = view.findViewById(R.id.fechaJuego)
            val hora : TextView = view.findViewById(R.id.horaJuego);
            val verResultado : TextView = view.findViewById(R.id.verResultados);
            val agregarResultados : TextView = view.findViewById(R.id.AgregarResultados);

            fun bind(vs: Vs) {
                verResultado.setOnClickListener {
                    // Asegúrate de que `id` no sea nulo antes de llamar a `onItemClick`
                    vs._id?.let { id ->
                        PasarIdvs.onVerResultados(id)
                    } ?: run {
                        Log.e("AdapterEquipos", "ID es nulo para el objeto Vs con fecha: ${vs.fecha}")
                    }
                }
                agregarResultados.setOnClickListener {
                    // Asegúrate de que `id` no sea nulo antes de llamar a `onItemClick`
                    vs._id?.let { id ->
                        PasarIdvs.OnAgregarResultados(id)
                    } ?: run {
                        Log.e("AdapterEquipos", "ID es nulo para el objeto Vs con fecha: ${vs.fecha}")
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): extendEquipos {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_equipos_planillero, parent, false)
            return extendEquipos(view)
        }

        override fun onBindViewHolder(holder: extendEquipos, position: Int) {
            val vs = vsList[position]
            Picasso.get().load(vs.equipo1.informacion.team1.Equipo.imgLogo).into(holder.logoEquipo1)
            Picasso.get().load(vs.equipo2.informacion.team2.Equipo.imgLogo).into(holder.logoEquipo2)

            holder.equipo1Name.text = vs.equipo1.informacion.team1.Equipo.nombreEquipo
            holder.equipo2Name.text = vs.equipo2.informacion.team2.Equipo.nombreEquipo
            holder.FechaJuego.text = vs.fecha
            holder.hora.text = vs.hora
            holder.bind(vs)
        }

        override fun getItemCount(): Int = vsList.size
}
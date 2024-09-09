package com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Participante
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Resultados

class VerResultadosEquipo1YEquipo2 (private val items: Resultados) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        const val Goles = 0
        const val Amarillas = 1
        const val Rojas = 2
        const val NoGoles = 3
        const val NoAmarillas = 4
        const val NoRojas = 5
    }
    override fun getItemViewType(position: Int): Int {
        val golesCont = items.equipo1.goles.jugadorGoleador.size
        val amarillasCount = items.equipo1.tarjetasAmarillas.size
        val rojasCount = items.equipo1.tarjetasRojas.size
        return when{
            golesCont == 0 && position == 0 -> NoGoles
            position < golesCont -> Goles
            golesCont == 0 && amarillasCount == 0 && position == 1 -> NoAmarillas
            position < golesCont + amarillasCount -> Amarillas
            golesCont == 0 && amarillasCount == 0 && rojasCount == 0 && position == 2 -> NoRojas
            position < golesCont + amarillasCount + rojasCount -> Rojas
            else -> throw IllegalArgumentException("Posición desconocida")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            Goles -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ver_goles_en_ver_resultados, parent, false)
                GolesViewHolder(view);
            }
            Amarillas -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_amarillas_ver_resultados, parent, false)
                AmarillasViewHolder(view);
            }
            Rojas -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_rojas_ver_resultados, parent, false)
                RojasViewHolder(view);
            }
            NoGoles -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_no_goles_ver_resultado, parent, false)
                NoGolesViewHolder(view);
            }
            NoAmarillas -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_no_goles_ver_resultado, parent, false)
                NoAmarillasViewHolder(view);
            }
            NoRojas -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_no_goles_ver_resultado, parent, false)
                NoRojasViewHolder(view);
            }
            else -> throw IllegalStateException("Revise")
        }
    }

    class NoRojasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(){
            itemView.findViewById<TextView>(R.id.textNoGoles).text = "Este equipo no hizo tarjetas rojas"
            itemView.findViewById<TextView>(R.id.TipoDeEstadistica).text = "Tarjetas rojas"
        }
    }

    class NoAmarillasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(){
            itemView.findViewById<TextView>(R.id.textNoGoles).text = "Este equipo no hizo tarjetas amarillas"
            itemView.findViewById<TextView>(R.id.TipoDeEstadistica).text = "Tarjetas rojas"
        }
    }
    class GolesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item : Participante){
            itemView.findViewById<TextView>(R.id.DorsalGoleadorVerResultados).text = item.dorsal.toString();
            itemView.findViewById<TextView>(R.id.NombreGoleadorVerResultados).text = item.nombres;
            itemView.findViewById<TextView>(R.id.FichaVerResultados).text = item.ficha.toString();
        }
    }
    class RojasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item : Participante){
            itemView.findViewById<TextView>(R.id.DorsalGoleadorVerResultadosTr).text = item.dorsal.toString();
            itemView.findViewById<TextView>(R.id.NombreGoleadorVerResultadosTR).text = item.nombres;
            itemView.findViewById<TextView>(R.id.FichaVerResultadosTR).text = item.ficha.toString();
        }
    }

    class AmarillasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item : Participante){
            itemView.findViewById<TextView>(R.id.DorsalGoleadorVerResultadosTA).text = item.dorsal.toString();
            itemView.findViewById<TextView>(R.id.NombreGoleadorVerResultadosTA).text = item.nombres;
            itemView.findViewById<TextView>(R.id.FichaVerResultadosTA).text = item.ficha.toString();
        }
    }

    class NoGolesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(){
            itemView.findViewById<TextView>(R.id.textNoGoles).text = "Este equipo no hizo gol"
            itemView.findViewById<TextView>(R.id.TipoDeEstadistica).text = "Goles"
        }
    }



    override fun getItemCount(): Int {
        val golesCount = items.equipo1.goles.jugadorGoleador.size
        val amarillasCount = items.equipo1.tarjetasAmarillas.size
        val rojasCount = items.equipo1.tarjetasRojas.size

        return when {
            golesCount == 0 && amarillasCount == 0 && rojasCount == 0 -> 3 // Mostrar "no goles", "no amarillas", "no rojas"
            golesCount == 0 && amarillasCount == 0 -> 2 // Mostrar "no goles" y "no amarillas"
            golesCount == 0 && rojasCount == 0 -> 2 // Mostrar "no goles" y "no rojas"
            amarillasCount == 0 && rojasCount == 0 -> golesCount + 2 // Mostrar goles y "no amarillas", "no rojas"
            golesCount == 0 -> 1 + amarillasCount + rojasCount // Mostrar "no goles" y amarillas y rojas
            else -> golesCount + amarillasCount + rojasCount
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val golesCount = items.equipo1.goles.jugadorGoleador.size
        val amarillasCount = items.equipo1.tarjetasAmarillas.size

        when (holder) {
            is GolesViewHolder -> {
                val jugadorGoleador = items.equipo1.goles.jugadorGoleador[position]
                holder.bind(jugadorGoleador)
            }
            is AmarillasViewHolder -> {
                val jugadorAmarilla = items.equipo1.tarjetasAmarillas[position - golesCount]
                holder.bind(jugadorAmarilla)
            }
            is RojasViewHolder -> {
                val jugadorRoja = items.equipo1.tarjetasRojas[position - golesCount - amarillasCount]
                holder.bind(jugadorRoja)
            }
            is NoGolesViewHolder -> holder.bind()
            is NoAmarillasViewHolder -> holder.bind()
            is NoRojasViewHolder -> holder.bind()
        }
    }

}
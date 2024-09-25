package com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luisavillacorte.gosportapp.R

class ResultadosAdapter(private val resultados: List<Resulatdos>) : RecyclerView.Adapter<ResultadosAdapter.ResultadoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultadoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resultadointer, parent, false)
        return ResultadoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultadoViewHolder, position: Int) {
        val partido = resultados[position]
        holder.bind(partido)
    }

    override fun getItemCount(): Int = resultados.size

    class ResultadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val equipo1Nombre: TextView = itemView.findViewById(R.id.nombre1)
        private val equipo2Nombre: TextView = itemView.findViewById(R.id.nombre2)
        private val golesEquipo1: TextView = itemView.findViewById(R.id.golin1)
        private val golesEquipo2: TextView = itemView.findViewById(R.id.golin2)
        private val amarillae1: TextView = itemView.findViewById(R.id.amain1)
        private val amarillae2: TextView = itemView.findViewById(R.id.amain2)
        private val logoEquipo1: ImageView = itemView.findViewById(R.id.logoe1)  // Imagen logo equipo 1
        private val logoEquipo2: ImageView = itemView.findViewById(R.id.logoe2)  // Imagen logo equipo 2

        fun bind(partido: Resulatdos) {
            // Asignación de los nombres de los equipos
            equipo1Nombre.text = partido.equipo1.equipo1?.nombreEquipo ?: "Equipo 1 no disponible"
            equipo2Nombre.text = partido.equipo2.equipo2?.nombreEquipo ?: "Equipo 2 no disponible"

            // Obtener y mostrar las amarillas para el equipo 1
            val amarillas1 = partido.equipo1.amarillasE1
            amarillae1.text = amarillas1?.joinToString(", ") { it.jugador.nombres } ?: "No hay amarillas"

            // Obtener y mostrar las amarillas para el equipo 2
            val amarillas2 = partido.equipo2.amarillasE2
            amarillae2.text = amarillas2?.joinToString(", ") { it.jugador.nombres } ?: "No hay amarillas"

            // Sumar los goles del equipo 1
            val goles1 = partido.equipo1.golesE1?.sumOf {
                it.goles.toIntOrNull() ?: 0
            } ?: 0

            // Sumar los goles del equipo 2
            val goles2 = partido.equipo2.golesE2?.sumOf {
                it.goles.toIntOrNull() ?: 0
            } ?: 0

            golesEquipo1.text = goles1.toString()
            golesEquipo2.text = goles2.toString()

            // Cargar los logos de los equipos usando Glide
            Glide.with(itemView.context)
                .load(partido.equipo1.equipo1.imgLogo) // Asegúrate de que 'imgLogo' esté disponible
                .into(logoEquipo1)

            Glide.with(itemView.context)
                .load(partido.equipo2.equipo2.imgLogo) // Asegúrate de que 'imgLogo' esté disponible
                .into(logoEquipo2)

            // Mostrar información sobre los jugadores y sus goles
            partido.equipo1.golesE1?.forEach { goleador ->
                Log.d("ResultadosAdapter", "Equipo 1 - Jugador: ${goleador.jugador.nombres}, Goles: ${goleador.goles}")
            }

            partido.equipo2.golesE2?.forEach { goleador ->
                Log.d("ResultadosAdapter", "Equipo 2 - Jugador: ${goleador.jugador.nombres}, Goles: ${goleador.goles}")
            }

            // Estado del partido
            Log.d("ResultadosAdapter", "Datos del partido: $partido")
        }
    }
}

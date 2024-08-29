package com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.planillero.helper.VerTarjetas
import com.luisavillacorte.gosportapp.planillero.viewActivities.Resultados.Fragment_Resultados
class JugadorViewHolder(itemView: View, private val fragment: Fragment_Resultados) : RecyclerView.ViewHolder(itemView) {
    // Referencias a los TextViews y botones
    private val nombreJugador: TextView = itemView.findViewById(R.id.nombresJugadoresR)
    private val dorsal: TextView = itemView.findViewById(R.id.dorsalParticipante)
    private val aumentarGol: ImageView = itemView.findViewById(R.id.añadirGol)
    private val disminuirGol: ImageView = itemView.findViewById(R.id.disminuirGol)
    private val masTarjetaAmarilla: ImageView = itemView.findViewById(R.id.tarjetaAmarillaMas)
    private val menosTarjetaAmarilla: ImageView = itemView.findViewById(R.id.tarjetaAmarillaMenos)
    private val masTarjetaRoja: ImageView = itemView.findViewById(R.id.tarjetaRojaMas)
    private val menosTarjetaRoja: ImageView = itemView.findViewById(R.id.tarjetaRojaMenos)
    private val marcadorTarjetaAmarilla: TextView = itemView.findViewById(R.id.ContadorTarjetaAmarilla)
    private val marcadorTarjetaRoja: TextView = itemView.findViewById(R.id.contadorTarjetaRoja)
    private val contadorGol: TextView = itemView.findViewById(R.id.ContadorGolesR)  // Ajusta según el ID real

    fun bind(jugador: Participante, viewModel: VerTarjetas, equipo: Int, idVs:String) {
        nombreJugador.text = jugador.nombreJugador
        dorsal.text = jugador.dorsal.toString()
        contadorGol.text = viewModel.getGoles(idVs, jugador._id, equipo ).toString()
        marcadorTarjetaAmarilla.text = viewModel.getTarjetasAmarillas(idVs, jugador._id, equipo).toString()
        marcadorTarjetaRoja.text = viewModel.getTarjetasRojas(idVs, jugador._id, equipo).toString()

        aumentarGol.setOnClickListener {
            viewModel.aumentarGol(idVs, jugador._id,equipo)
            contadorGol.text = viewModel.getGoles(idVs, jugador._id, equipo) .toString()
            fragment.actualizarContadoresGenerales(equipo, idVs)

            Toast.makeText(
                itemView.context,
                "Gol anotado por: ${jugador.nombreJugador}\n" ,
                Toast.LENGTH_SHORT
            ).show()
        }

        disminuirGol.setOnClickListener {
            val exito = viewModel.disminuirGol(idVs,jugador._id, equipo)
            if (exito) {
                contadorGol.text = viewModel.getGoles(idVs , jugador._id, equipo).toString()
                fragment.actualizarContadoresGenerales(equipo, idVs)
                Toast.makeText(
                    itemView.context,
                    "Gol descontado por: ${jugador.nombreJugador}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    itemView.context,
                    "No se porque menos ${jugador.nombreJugador}. Ya tiene 0 goles.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        masTarjetaAmarilla.setOnClickListener {
            val amarillas = viewModel.getTarjetasAmarillas(idVs,jugador._id, equipo)
            if (amarillas<2){
                viewModel.aumentarTarjetaAmarilla(idVs,jugador._id, equipo)
                marcadorTarjetaAmarilla.text = (amarillas +1 ).toString()
                fragment.actualizarContadoresGenerales(equipo, idVs)
                Toast.makeText(
                    itemView.context,
                    "Tarjeta Amarilla anotada por: ${jugador.nombreJugador}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    itemView.context,
                    "No se pueden agregar más de dos tarjetas amarillas para ${jugador.nombreJugador}",
                    Toast.LENGTH_SHORT
                ).show()
            }

       }

        menosTarjetaAmarilla.setOnClickListener {
            val exito = viewModel.disminuirTarjetaAmarilla(idVs,jugador._id, equipo)
            if (exito){
                marcadorTarjetaAmarilla.text = viewModel.getTarjetasAmarillas(idVs,jugador._id, equipo).toString()
                fragment.actualizarContadoresGenerales(equipo, idVs)
                Toast.makeText(
                    itemView.context,
                    "Tarjeta Amarilla --  por: ${jugador.nombreJugador}",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    itemView.context,
                    "No se porque menos ${jugador.nombreJugador}. Ya tiene 0 tarjetas amarillas.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        masTarjetaRoja.setOnClickListener {

           val rojas = viewModel.getTarjetasRojas(idVs,jugador._id, equipo);

            if (rojas<1){
                viewModel.aumentarTarjetaRoja(idVs, jugador._id, equipo)
                marcadorTarjetaRoja.text = (rojas +1).toString()
                fragment.actualizarContadoresGenerales(equipo, idVs)
                Toast.makeText(
                    itemView.context,
                    "Tarjeta ROJA anotada por: ${jugador.nombreJugador}",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(itemView.context,"No se puede añadir mas rojas ${jugador.nombreJugador}", Toast.LENGTH_SHORT).show();
            }
        }

        menosTarjetaRoja.setOnClickListener {
            val exito =  viewModel.disminuirTarjetaRoja(idVs,jugador._id, equipo)
            if (exito){
                marcadorTarjetaRoja.text = viewModel.getTarjetasRojas(idVs, jugador._id,equipo).toString()
                fragment.actualizarContadoresGenerales(equipo,idVs)
                Toast.makeText(
                    itemView.context,
                    "Tarjeta Roja --- por: ${jugador.nombreJugador}",
                    Toast.LENGTH_SHORT
                ).show()
            }else{
                Toast.makeText(
                    itemView.context,
                    "No se porque menos ${jugador.nombreJugador}. Ya tiene 0 tarjetas rojas.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}
class ParticipanteAdapter(
    private val participantes: List<Participante>,
    private val viewModel: VerTarjetas,
    private val fragment: Fragment_Resultados,
    private val equipo: Int,
    private val idVs: String
) : RecyclerView.Adapter<JugadorViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jugadores_resultado, parent, false)
        return JugadorViewHolder(view, fragment)
    }


    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        holder.bind(participantes[position], viewModel, equipo, idVs)
    }

    override fun getItemCount(): Int {
        return participantes.size
    }
}


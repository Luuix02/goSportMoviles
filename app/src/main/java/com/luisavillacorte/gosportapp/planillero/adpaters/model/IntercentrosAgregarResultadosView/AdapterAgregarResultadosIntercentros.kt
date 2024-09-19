package com.luisavillacorte.gosportapp.planillero.adpaters.model.IntercentrosAgregarResultadosView

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Participantes
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.ParticipantesInter
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.JugadorViewHolder
import com.luisavillacorte.gosportapp.planillero.helper.VerTarjetas
import com.luisavillacorte.gosportapp.planillero.viewActivities.AgregarResultadosIntercenntros.Fragment_AgregarResultadosIntercentros
import com.luisavillacorte.gosportapp.planillero.viewActivities.Resultados.Fragment_Resultados
class JugadorViewHolderInter(itemView: View, private val fragment: Fragment_AgregarResultadosIntercentros) : RecyclerView.ViewHolder(itemView) {
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
    private val contadorGol: TextView = itemView.findViewById(R.id.ContadorGolesR)
    private val checkJugadorDestacado: CheckBox = itemView.findViewById(R.id.CheckJugadorDestacado);

    fun bind(jugador: ParticipantesInter, viewModel: VerTarjetas, equipo: Int, idVs:String) {
        nombreJugador.text = jugador.nombres
        dorsal.text = jugador.dorsal.toString()
        contadorGol.text = viewModel.getGoles(idVs, jugador._id, equipo ).toString()
        marcadorTarjetaAmarilla.text = viewModel.getTarjetasAmarillas(idVs, jugador._id, equipo).toString()
        marcadorTarjetaRoja.text = viewModel.getTarjetasRojas(idVs, jugador._id, equipo).toString()

        val listaDestacados = viewModel.jugadoresDestacados.value?.get(idVs) ?: mutableListOf()
        checkJugadorDestacado.isChecked = listaDestacados.contains(jugador._id)

        checkJugadorDestacado.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.agregarJugadorDestacado(idVs, jugador._id)
                Log.d("JugadorDestacado", "jugadorDestacado ${jugador._id}")
            } else {
                viewModel.quitarJugadorDestacado(idVs, jugador._id)
            }
        }

        aumentarGol.setOnClickListener {
            viewModel.aumentarGol(idVs, jugador._id,equipo)
            contadorGol.text = viewModel.getGoles(idVs, jugador._id, equipo) .toString()
            fragment.actualizarContadoresGenerales(equipo, idVs)
        }

        disminuirGol.setOnClickListener {
            val exito = viewModel.disminuirGol(idVs,jugador._id, equipo)
            if (exito) {
                contadorGol.text = viewModel.getGoles(idVs , jugador._id, equipo).toString()
                fragment.actualizarContadoresGenerales(equipo, idVs)
                Log.d("Gol", "\"Gol descontado por: ${jugador.nombres}\",")
            } else {
                Toast.makeText(
                    itemView.context,
                    "Ya tiene 0 goles.",
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

                Log.d("Ta", "Tarjeta Amarilla anotada por: ${jugador.nombres}")
            } else {
                Toast.makeText(
                    itemView.context,
                    "Maximo 2 tarjetas amarillas",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        menosTarjetaAmarilla.setOnClickListener {
            val exito = viewModel.disminuirTarjetaAmarilla(idVs,jugador._id, equipo)
            if (exito){
                marcadorTarjetaAmarilla.text = viewModel.getTarjetasAmarillas(idVs,jugador._id, equipo).toString()
                fragment.actualizarContadoresGenerales(equipo, idVs)
                Log.d("Ta", "Tarjeta --- amarillas anotada por: ${jugador.nombres}")

            }else{
                Toast.makeText(
                    itemView.context,
                    "Ya tiene 0 tarjetas amarillas.",
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
                Log.d("Ta", "Tarjeta Roja anotada por: ${jugador.nombres}")
            }else{
                Toast.makeText(itemView.context,"Maximo 1 tarejeta roja", Toast.LENGTH_SHORT).show();
            }
        }

        menosTarjetaRoja.setOnClickListener {
            val exito =  viewModel.disminuirTarjetaRoja(idVs,jugador._id, equipo)
            if (exito){
                marcadorTarjetaRoja.text = viewModel.getTarjetasRojas(idVs, jugador._id,equipo).toString()
                fragment.actualizarContadoresGenerales(equipo,idVs)
                Log.d("Ta", "Tarjeta ....Roja anotada por: ${jugador.nombres}")

            }else{
                Toast.makeText(
                    itemView.context,
                    " Ya tiene 0 tarjetas rojas.",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}
class AdapterAgregarResultadosIntercentros(
    private val participantes: List<ParticipantesInter>,
    private val viewModel: VerTarjetas,
    private val fragment: Fragment_AgregarResultadosIntercentros,
    private val equipo: Int,
    private val idVs: String
) : RecyclerView.Adapter<JugadorViewHolderInter>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolderInter {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jugadores_resultado, parent, false)
        return JugadorViewHolderInter(view, fragment)
    }


    override fun onBindViewHolder(holder: JugadorViewHolderInter, position: Int) {
        holder.bind(participantes[position], viewModel, equipo, idVs)
    }

    override fun getItemCount(): Int {
        return participantes.size
    }
}
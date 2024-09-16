package com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.luisavillacorte.gosportapp.R
import com.squareup.picasso.Picasso

class ResultadosDialogFragment : DialogFragment(){

    companion object {
        private const val ARG_RESULTADOS = "resultados"

        fun newInstance(resultados: ResultadosResponse): ResultadosDialogFragment {
            val fragment = ResultadosDialogFragment()
            val args = Bundle()
            args.putParcelable(ARG_RESULTADOS, resultados)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.modalverresultados, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resultados: ResultadosResponse? = arguments?.getParcelable(ARG_RESULTADOS)

        val imgEquipo1: ImageView = view.findViewById(R.id.imgEquipo1)
        val marcador1: TextView = view.findViewById(R.id.marcador1)
        val nombreJugGol1: TextView = view.findViewById(R.id.nombreJugGol1)
        val amarilla1: ImageView = view.findViewById(R.id.amarilla1)
        val nombreJugAmarilla1: TextView = view.findViewById(R.id.nombreJugAmarilla1)
        val roja1: ImageView = view.findViewById(R.id.roja1)
        val nombreJugRoja1: TextView = view.findViewById(R.id.nombreJugRoja1)

        val imgEquipo2: ImageView = view.findViewById(R.id.imgEquipo2)
        val marcador2: TextView = view.findViewById(R.id.marcador2)
        val nombreJugGol2: TextView = view.findViewById(R.id.nombreJugGol2)
        val amarilla2: ImageView = view.findViewById(R.id.amarilla2)
        val nombreJugAmarilla2: TextView = view.findViewById(R.id.nombreJugAmarilla2)
        val roja2: ImageView = view.findViewById(R.id.roja2)
        val nombreJugRoja2: TextView = view.findViewById(R.id.nombreJugRoja2)
        val defaultImg = R.drawable.logosinfondo

        resultados?.let { resultadosNonNull ->
            // Configurar equipo 1
            resultadosNonNull.equipo1?.let { equipo1 ->
                val logoEquipo1Url = equipo1.equipo?.imgLogo
                Log.d("ResultadosDialogFragment", "URL Equipo 1: $logoEquipo1Url")
                if (logoEquipo1Url.isNullOrEmpty()) {
                    imgEquipo1.setImageResource(defaultImg)
                } else {
                    Picasso.get().load(logoEquipo1Url).placeholder(defaultImg).error(defaultImg).into(imgEquipo1, object : com.squareup.picasso.Callback{
                        override fun onSuccess() {
                            Log.d("ResultadosDialogFragment", "Logo de equipo 1 cargado correctamente")
                        }
                        override fun onError(e: Exception?) {
                            Log.e("ResultadosDialogFragment", "Error al cargar el logo de equipo 1", e)
                        }
                    })
                }
                marcador1.text = equipo1.goles.marcador.toString() ?: "0"
                nombreJugGol1.text = equipo1.goles.jugadorGoleador.joinToString(", ") { jugador -> jugador.nombres }
                    ?: "No hay goleador"
                amarilla1.visibility = if (equipo1.tarjetasAmarillas?.isNotEmpty() == true) View.VISIBLE else View.GONE
                nombreJugAmarilla1.text = equipo1.tarjetasAmarillas?.joinToString(", ") { tarjeta -> tarjeta.nombres } ?: "No hay tarjetas amarillas"
                roja1.visibility = if (equipo1.tarjetasRojas?.isNotEmpty() == true) View.VISIBLE else View.GONE
                nombreJugRoja1.text = equipo1.tarjetasRojas?.joinToString(", ") { tarjeta -> tarjeta.nombres } ?: "No hay tarjetas rojas"
            } ?: run {
                imgEquipo1.setImageResource(defaultImg)
                marcador1.text = "0"
                nombreJugGol1.text = "No hay goleador"
                amarilla1.visibility = View.GONE
                nombreJugAmarilla1.text = "No hay tarjetas amarillas"
                roja1.visibility = View.GONE
                nombreJugRoja1.text = "No hay tarjetas rojas"
            }

            // Configurar equipo 2
            resultadosNonNull.equipo2?.let { equipo2 ->
                val logoEquipo2Url = equipo2.equipo?.imgLogo
                Log.d("ResultadosDialogFragment", "URL Equipo 2: $logoEquipo2Url")
                if (logoEquipo2Url.isNullOrEmpty()) {
                    imgEquipo2.setImageResource(defaultImg)
                } else {
                    Picasso.get().load(logoEquipo2Url).placeholder(defaultImg).error(defaultImg).into(imgEquipo2)
                }
                marcador2.text = equipo2.goles?.marcador?.toString() ?: "0"
                nombreJugGol2.text = equipo2.goles?.jugadorGoleador?.joinToString(", ") { jugador -> jugador.nombres } ?: "No hay goleador"
                amarilla2.visibility = if (equipo2.tarjetasAmarillas?.isNotEmpty() == true) View.VISIBLE else View.GONE
                nombreJugAmarilla2.text = equipo2.tarjetasAmarillas?.joinToString(", ") { tarjeta -> tarjeta.nombres } ?: "No hay tarjetas amarillas"
                roja2.visibility = if (equipo2.tarjetasRojas?.isNotEmpty() == true) View.VISIBLE else View.GONE
                nombreJugRoja2.text = equipo2.tarjetasRojas?.joinToString(", ") { tarjeta -> tarjeta.nombres } ?: "No hay tarjetas rojas"
            } ?: run {
                // Manejo del caso en que equipo2 es null
                imgEquipo2.setImageResource(defaultImg)
                marcador2.text = "0"
                nombreJugGol2.text = "No hay goleador"
                amarilla2.visibility = View.GONE
                nombreJugAmarilla2.text = "No hay tarjetas amarillas"
                roja2.visibility = View.GONE
                nombreJugRoja2.text = "No hay tarjetas rojas"
            }
        } ?: run {
            // Manejo del caso en que resultados es null
            Log.e("ResultadosDialogFragment", "Resultados es null")
        }
    }
}
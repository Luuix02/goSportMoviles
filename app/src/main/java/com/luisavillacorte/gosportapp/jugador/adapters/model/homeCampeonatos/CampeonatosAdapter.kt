package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import android.content.Context
import android.sax.EndElementListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo

class CampeonatosAdapter(

    private val campeonatos: List<Campeonatos>,
//    private val equipoActual: Equipo?,
//    private val esCapitan: Boolean,
    private val presenter: HomeCampeonatosContract.Presenter


    ) : RecyclerView.Adapter<CampeonatosAdapter.CampeonatoViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CampeonatoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_campeonato, parent, false)
        return CampeonatoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CampeonatoViewHolder, position: Int) {
        val campeonato = campeonatos[position]
        holder.bind(campeonato)
    }

    override fun getItemCount(): Int = campeonatos.size

    inner class CampeonatoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nombreCampeonato: TextView = itemView.findViewById(R.id.nombreCampeonato)
        private val descripcion: TextView = itemView.findViewById(R.id.descripcion)
        private val btnVerDetalles: Button = itemView.findViewById(R.id.btnVerDetalles)
        private val btnincribir: TextView = itemView.findViewById(R.id.btnincribirme)
        private  val estadocampeonato: TextView=itemView.findViewById(R.id.estado)
        private val categoria:TextView=itemView.findViewById(R.id.categoria)
        private val fechaini:TextView=itemView.findViewById(R.id.fechaInicio)
        private val fechafinal:TextView=itemView.findViewById(R.id.fechaFin)
        private val tipo:TextView=itemView.findViewById(R.id.tipocampeonato)

        fun bind(campeonato: Campeonatos) {
            nombreCampeonato.text = campeonato.nombreCampeonato
            descripcion.text = campeonato.descripcion
            estadocampeonato.text=campeonato.estadoCampeonato
            categoria.text=campeonato.nombreDisciplinas
            fechaini.text=campeonato.fechaInicio
            fechafinal.text=campeonato.fechaFin
            tipo.text=campeonato.tipoCampeonato

            val sharedPreferences = itemView.context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            val identificacion = sharedPreferences.getString("CEDULA", "")

            if (campeonato.estadoCampeonato == "Inscripcion") {
                btnincribir.visibility = View.VISIBLE
                btnincribir.setOnClickListener {
                    if (identificacion.isNullOrEmpty()) {
                        Toast.makeText(itemView.context, "Identificación no disponible", Toast.LENGTH_SHORT).show()
                    } else {
                        presenter.verificarEquipoEnCampeonato(identificacion) { isInscrito ->
                            if (isInscrito) {
                                Toast.makeText(itemView.context, "El equipo ya está inscrito en este campeonato.", Toast.LENGTH_SHORT).show()
                            } else {
                                presenter.inscribirEquipoEnCampeonato(campeonato._id)
                            }
                        }
                    }
                }
            } else {
                btnincribir.visibility = View.GONE
            }

            // Siempre ocultar el botón "Ver Detalles"
            btnVerDetalles.visibility = View.GONE




//            when (campeonato.estadoCampeonato) {
//                "Ejecucion" -> {
//                    btnVerDetalles.visibility = View.VISIBLE
//                    btnincribir.visibility = View.GONE
//                    // Configurar el botón para llevar a otra actividad
//                    btnVerDetalles.setOnClickListener {
//                        // Código para navegar a la página de detalles
//                    }
//                }
//                "Inscripcion" -> {
//                    btnincribir.visibility = View.VISIBLE
//                    btnVerDetalles.visibility = View.GONE
//                }
//                else -> {
//                    // Si hay otros estados, ocultar ambos
//                    btnVerDetalles.visibility = View.GONE
//                    btnincribir.visibility = View.GONE
//                }
//            }
        }

    }
}


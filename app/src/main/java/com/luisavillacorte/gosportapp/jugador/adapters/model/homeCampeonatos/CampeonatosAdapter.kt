package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import android.content.Context
import android.sax.EndElementListener
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo


class CampeonatosAdapter(

    private val campeonatos: List<Campeonatos>,
//    private val equipoActual: Equipo?,
//    private val esCapitan: Boolean,
//    private val View: HomeCampeonatosContract.View,
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
        private val estadocampeonato: TextView = itemView.findViewById(R.id.estado)
        private val categoria: TextView = itemView.findViewById(R.id.categoria)
        private val fechaini: TextView = itemView.findViewById(R.id.fechaInicio)
        private val fechafinal: TextView = itemView.findViewById(R.id.fechaFin)


        fun bind(campeonato: Campeonatos) {
            nombreCampeonato.text = campeonato.nombreCampeonato
            descripcion.text = campeonato.descripcion
            estadocampeonato.text = campeonato.estadoCampeonato
            categoria.text = campeonato.nombreDisciplinas
            fechaini.text = campeonato.fechaInicio
            fechafinal.text = campeonato.fechaFin

//            val sharedPreferences = itemView.context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
//            val idCampeonatoInscrito = sharedPreferences.getString("CAMPEONATO_INSCRITO_ID", null)
//            Log.d("CampeonatosAdapter", "ID del campeonato recuperado: $idCampeonatoInscrito")

            if (campeonato.estadoCampeonato == "Inscripcion") {
                btnincribir.visibility = View.VISIBLE
                btnincribir.setOnClickListener {
                    val equipo = obtenerEquipoDeSharedPreferences(itemView.context)
                    if (equipo == null) {
                        Toast.makeText(
                            itemView.context,
                            "No tienes un equipo registrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Verificar si el equipo ya está inscrito antes de mostrar el diálogo
                        presenter.verificarEquipoEnCampeonato(equipo.cedula) { isInscrito, msg ->
                            if (isInscrito) {
                                Toast.makeText(
                                    itemView.context,
                                    "El equipo ya está inscrito en otro campeonato.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                mostrarModalConfirmacion(campeonato._id, equipo)
                            }
                        }
                    }
                }
            } else {
                btnincribir.visibility = View.GONE
            }

//            if (campeonato._id == idCampeonatoInscrito){
//                btnincribir.text = "Ya estás inscrito"
//                btnincribir.isEnabled = false
//            }
        }


        private fun mostrarModalConfirmacion(idCampeonato: String, equipo: Equipo) {
            AlertDialog.Builder(itemView.context)
                .setMessage("¿Estás seguro de que deseas inscribir tu equipo a este campeonato?")
                .setPositiveButton("Aceptar") { _, _ ->
                    Log.d("CampeonatosAdapter", "Equipo en confirmación: $equipo")
                    presenter.verificarEquipoEnCampeonato(equipo.cedula) { isInscrito, msg ->
                        if (isInscrito) {
                            Toast.makeText(itemView.context, "El equipo ya está inscrito en otro campeonato.", Toast.LENGTH_SHORT).show()
                        } else {
                            presenter.inscribirEquipoEnCampeonato(equipo, idCampeonato)
                        }
                    }
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        private fun obtenerEquipoDeSharedPreferences(context: Context): Equipo? {
            val sharedPreferences =
                context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            val equipoJson = sharedPreferences.getString("EQUIPO_DATA", null)
            Log.d("SharedPreferences", "Equipo JSON recuperado: $equipoJson")
            return if (equipoJson != null) {
                val gson = Gson()
                gson.fromJson(equipoJson, Equipo::class.java)
            } else {
                null
            }
        }
    }
    fun guardarIdCampeonatoEquipoInscrito(context: Context, idCampeonato: String) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("CAMPEONATO_INSCRITO_ID", idCampeonato)
        editor.apply()
        Log.d("SharedPreferences", "ID guardado: $idCampeonato")
    }


}
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



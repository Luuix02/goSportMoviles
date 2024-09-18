package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.interCentros

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.Equipo as InterCentrosEquipo // Renombrar para evitar conflicto
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.InterCentrosContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros.InterCentrosModel
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo as CrearEquipo // Renombrar para evitar conflicto
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscriptoResponse

class InterCentrosPresenter(
    private val view: InterCentrosContract.View,
    private val model: InterCentrosModel,
    private val context: Context // Necesitamos el contexto para obtener SharedPreferences
) : InterCentrosContract.Presenter {

    override fun loadPartidos(equipoId: String) {
        model.getPartidosJugados(equipoId) { result ->
            result.onSuccess { partidos ->
                view.showPartidos(partidos)
                Log.d("InterCentrosPresenter", "Partidos obtenidos: $partidos")
            }
            result.onFailure { error ->
                view.showError(error.message ?: "Error desconocido")
                Log.e("InterCentrosPresenter", "Error al obtener partidos jugados: ${error.message}")
            }
        }
    }

    override fun loadEquiposInscritos(idCampeonato: String) {
        model.obtenerEquiposInscritos(idCampeonato) { result ->
            result.onSuccess { equiposInscritos ->
                // Manejar la lista de equipos inscritos
                Log.d("InterCentrosPresenter", "Equipos inscritos: ${equiposInscritos.map { it.Equipo.nombreEquipo }}")
                view.showEquiposInscritos(equiposInscritos)

                // Aquí podrías guardar el primer equipo en SharedPreferences como ejemplo
                equiposInscritos.firstOrNull()?.let { equipo ->
                    guardarEquipoEnSharedPreferences(context, equipo.Equipo)
                }
            }
            result.onFailure { exception ->
                // Manejar el error
                view.showError(exception.message ?: "Error desconocido")
                Log.e("InterCentrosPresenter", "Error al obtener equipos inscritos: ${exception.message}")
            }
        }
    }

    // Método para guardar el objeto Equipo completo en SharedPreferences
    private fun guardarEquipoEnSharedPreferences(context: Context, equipo: CrearEquipo) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val equipoJson = gson.toJson(equipo) // Convertir el objeto equipo a JSON
        editor.putString("EQUIPO", equipoJson) // Guardar el JSON del equipo
        editor.apply()
        Log.d("InterCentrosPresenter", "Equipo guardado en SharedPreferences: $equipoJson")
    }

    // Método para obtener el objeto Equipo desde SharedPreferences
    private fun obtenerEquipoDeSharedPreferences(context: Context): CrearEquipo? {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val equipoJson = sharedPreferences.getString("EQUIPO", null)
        return if (equipoJson != null) {
            val gson = Gson()
            val equipo = gson.fromJson(equipoJson, CrearEquipo::class.java) // Deserializar el JSON a un objeto Equipo
            Log.d("InterCentrosPresenter", "Equipo obtenido de SharedPreferences: $equipo")
            equipo
        } else {
            Log.d("InterCentrosPresenter", "No se encontró el equipo en SharedPreferences.")
            null
        }
    }
}

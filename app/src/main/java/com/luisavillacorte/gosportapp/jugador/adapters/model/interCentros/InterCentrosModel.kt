package com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros

import android.util.Log
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscritoData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InterCentrosModel {

    private val apiService = RetrofitInstance.createService(HomeApiService::class.java)

    // Método para obtener equipos inscritos
    fun obtenerEquiposInscritos(idCampeonato: String, callback: (Result<List<EquipoInscritoData>>) -> Unit) {
        apiService.obtenerEquiposInscritos(idCampeonato).enqueue(object : Callback<List<EquipoInscritoData>> {
            override fun onResponse(call: Call<List<EquipoInscritoData>>, response: Response<List<EquipoInscritoData>>) {
                if (response.isSuccessful) {
                    val equiposInscritos = response.body() ?: emptyList()
                    callback(Result.success(equiposInscritos))
                    Log.d("InterCentrosModel", "Respuesta de equipos inscritos: ${equiposInscritos.map { it.Equipo.nombreEquipo }}")
                } else {
                    callback(Result.failure(Exception("Error: ${response.code()} - ${response.message()}")))
                    Log.e("InterCentrosModel", "Error al obtener equipos inscritos: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<EquipoInscritoData>>, t: Throwable) {
                callback(Result.failure(t))
                Log.e("InterCentrosModel", "Falló la solicitud de equipos inscritos: ${t.message}")
            }
        })
    }

    // Método para obtener partidos jugados
    fun getPartidosJugados(equipoId: String, callback: (Result<List<Partidos>>) -> Unit) {
        apiService.getPartidosJugados(equipoId).enqueue(object : Callback<List<Partidos>> {
            override fun onResponse(call: Call<List<Partidos>>, response: Response<List<Partidos>>) {
                if (response.isSuccessful) {
                    val partidos = response.body() ?: emptyList()
                    callback(Result.success(partidos))
                    Log.d("InterCentrosModel", "Respuesta de partidos jugados: ${partidos}")
                } else {
                    callback(Result.failure(Exception("Error: ${response.code()} - ${response.message()}")))
                    Log.e("InterCentrosModel", "Error al obtener partidos jugados: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Partidos>>, t: Throwable) {
                callback(Result.failure(t))
                Log.e("InterCentrosModel", "Falló la solicitud de partidos jugados: ${t.message}")
            }
        })
    }
}

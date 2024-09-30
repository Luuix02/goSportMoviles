package com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos

import android.content.Context
import android.util.Log
import androidx.compose.runtime.traceEventEnd
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.misPartidosService.MisPartidosApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.recuperarIdCampeonatoEquipoInscrito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MisPartidosPresenter(
    private val view: MisPartidosContract.View,
    private val apiService: MisPartidosApiService,
    private val context: Context
) : MisPartidosContract.Presenter {
    override fun verificarTipoCampeonatoYRedirigir() {
        val idCampeonato = recuperarIdCampeonatoEquipoInscrito(context)
        Log.d("IDCampeonato", "ID del campeonato recuperado: $idCampeonato")

        if (idCampeonato != null) {
            val call = apiService.getDetalleCampeonato(idCampeonato)
            Log.d("Request", "URL de la solicitud: ${call.request().url}")
            call.enqueue(object : Callback<Campeonatos> {
                override fun onResponse(call: Call<Campeonatos>, response: Response<Campeonatos>) {
                    Log.d("Response", "Código de respuesta: ${response.code()}")
                    if (response.isSuccessful) {
                        val campeonato = response.body()
                        Log.d("Campeonato", "Cuerpo de la respuesta: $campeonato")
                        if (campeonato != null) {
                            val tipoCampeonato = campeonato.tipoCampeonato
                            Log.d("TipoCampeonato", "Tipo de campeonato: $tipoCampeonato")
                            when (tipoCampeonato) {
                                "Intercentros" -> {
                                    view.navegarAIntercentros()
                                }

                                "Interfichas", "Recreativos" -> {
                                    view.navegarAMisPartidos()
                                }

                                else -> {
                                    view.showError("Tipo de campeonato desconocido")
                                }
                            }
                        } else {
                            Log.e("ErrorAPI", "Cuerpo nulo en la respuesta")
                            view.showError("Error: no se pudo obtener el campeonato")
                        }
                    } else {
                        Log.e(
                            "ErrorAPI",
                            "Error en la respuesta: ${response.code()} - ${response.message()}"
                        )
                        view.showError("Error al obtener detalles del campeonato. Código: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Campeonatos>, t: Throwable) {
                    Log.e("ErrorAPI", "Error de red: ${t.message}")
                    view.showError("Error de red: ${t.message}")
                }
            })
        } else {
            view.showError("ID de campeonato no encontrado")
        }
    }

    override fun obtenerVsEquipo() {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val equipoId = sharedPreferences.getString("EQUIPO_ID", null)
        val idCampeonato = recuperarIdCampeonatoEquipoInscrito(context)
        Log.d("IDCampeonato", "ID del campeonato recuperado: $idCampeonato")

        if (equipoId != null && idCampeonato != null) {
            val call = apiService.obtenerVsMiEquipo(equipoId)
            call.enqueue(object : Callback<List<VsResponse>> {
                override fun onResponse(
                    call: Call<List<VsResponse>>,
                    response: Response<List<VsResponse>>
                ) {
                    if (response.isSuccessful) {
                        val vsList = response.body()
                        if (vsList != null) {
                            val vsFiltrados = vsList.filter {
                                it.idCampeonato == idCampeonato
                            }
                            if (vsFiltrados.isNotEmpty()) {
                                view.mostrarVs(vsFiltrados)
                                val idVs = vsFiltrados.firstOrNull()?._id
                                if (idVs != null) {
                                    saveIdVsInPreferences(idVs)
                                }
                            } else {
                                view.showError("No hay partidos disponibles para el campeonato actual.")
                            }
                        } else {
                            view.showError("No se encontraron resultados.")
                        }
                    } else {
                        view.showError("Error en la respuesta del servidor.")
                    }
                }

                override fun onFailure(call: Call<List<VsResponse>>, t: Throwable) {
                    view.showError("Error al conectar con el servidor: ${t.message}")
                }
            })
        } else {
            view.mostrarMensaje("Aún no estás inscrito a un campeonato. Una vez que te unas, podrás visualizar los partidos y resultados de tu equipo.")
        }
    }



    override fun cargarResultados(idVs: String) {
        val call = apiService.obtenerResultadosMiEquipoVs(idVs)
            .enqueue(object : Callback<ResultadosResponse> {
                override fun onResponse(
                    call: Call<ResultadosResponse>,
                    response: Response<ResultadosResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { resultados ->
                            view.mostrarResultados(resultados)
                        } ?: view.showError("Respuesta vacía")
                    } else {
                        view.showError("Error en la respuesta: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ResultadosResponse>, t: Throwable) {
                    view.showError("Error de red: ${t.message}")
                }
            })
    }

    fun recuperarIdCampeonatoEquipoInscrito(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val idCampeonato = sharedPreferences.getString("CAMPEONATO_INSCRITO_ID", null)
        Log.d("SharedPreferences", "ID recuperado: $idCampeonato")

        return idCampeonato
    }


    private fun saveIdVsInPreferences(idVs: String) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ID_VS", idVs)
        editor.apply()
    }


}


package com.luisavillacorte.gosportapp.jugador.adapters.model.misPartidos

import android.content.Context
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.misPartidosService.MisPartidosApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MisPartidosPresenter(
    private val view: MisPartidosContract.View,
    private val apiService: MisPartidosApiService,
    private val context: Context
) : MisPartidosContract.Presenter {

    override fun obtenerVsEquipo() {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val equipoId = sharedPreferences.getString("EQUIPO_ID", null)

        if (equipoId != null) {
            val call = apiService.obtenerVsMiEquipo(equipoId)
            call.enqueue(object : Callback<List<VsResponse>> {
                override fun onResponse(
                    call: Call<List<VsResponse>>,
                    response: Response<List<VsResponse>>
                ) {
                    if (response.isSuccessful) {
                        val vsList = response.body()
                        if (vsList != null) {
                            view.mostrarVs(vsList)
                            val idVs = vsList.firstOrNull()?._id
                            if (idVs != null){
                                saveIdVsInPreferences(idVs)
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
            view.showError("ID de equipo no encontrado en las preferencias.")

        }
    }

    override fun cargarResultados(idVs: String) {
        val call = apiService.obtenerResultadosMiEquipoVs(idVs).enqueue(object : Callback<ResultadosResponse> {
            override fun onResponse(call: Call<ResultadosResponse>, response: Response<ResultadosResponse>) {
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

    private fun saveIdVsInPreferences(idVs: String) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ID_VS", idVs)
        editor.apply()
    }


}


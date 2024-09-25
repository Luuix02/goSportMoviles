package com.luisavillacorte.gosportapp.jugador.adapters.model.campIntercentros

import android.content.ContentValues.TAG
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos.Campeonatos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CampeonatoInterPresenter (

    private val view: CampeonatintContract.View,
    private val apiService: HomeApiService
) :CampeonatintContract.Presenter{



    override fun obtenerCampeonatos() {
        view.showLoading()
        Log.d(TAG, "Fetching campeonatos from API")

        val call = apiService.traerCampeonatosIntercentros()
        call.enqueue(object : Callback<List<CampeonatInter>> {
            override fun onResponse(
                call: Call<List<CampeonatInter>>,
                response: Response<List<CampeonatInter>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { campeonatos ->
                        val campeonatoFiltrado = campeonatos.filter {
                            it.estadoCampeonato == "Inscripcion" || it.estadoCampeonato == "Ejecucion" || it.estadoCampeonato == "Finalizado"
                        }

                        view.mostrarCampeonatos(campeonatoFiltrado)
                        Log.d(TAG, "Campeonatos filtered and shown: ${campeonatoFiltrado.size}")
                    } ?: view.showError("No se recibieron campeonatos.")
                } else {
                    view.showError("Error: ${response.code()}")
                    Log.e(TAG, "Error response code: ${response.code()}")
                }
                view.hideLoading()
            }

            override fun onFailure(call: Call<List<CampeonatInter>>, t: Throwable) {
                view.hideLoading()
                view.showError(t.message ?: "Error desconocido")
                Log.e(TAG, "API call failed: ${t.message}")
            }
        })

    }


}
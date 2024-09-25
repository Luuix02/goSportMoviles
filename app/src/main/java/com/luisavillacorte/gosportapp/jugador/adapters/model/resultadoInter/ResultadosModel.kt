package com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter

import android.util.Log
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultadosModel : ResultadosContract.Model {

    private val apiService = RetrofitInstance.createService(HomeApiService::class.java)
    private val TAG = "ResultadosModel"

    override fun fetchResultados(idCampeonato: String, onFinished: ResultadosContract.Model.OnFinishedListener) {
        apiService.getResultados(idCampeonato).enqueue(object : Callback<List<Resulatdos>> {
            override fun onResponse(call: Call<List<Resulatdos>>, response: Response<List<Resulatdos>>) {
                if (response.isSuccessful && response.body() != null) {
                    Log.d(TAG, "Respuesta cruda de la API: ${response.raw().toString()}") // Imprimir la respuesta cruda
                    Log.d(TAG, "Respuesta de la API exitosa: ${response.body()}")
                    onFinished.onSuccess(response.body()!!)
                } else {
                    Log.e(TAG, "Error en la respuesta de la API: ${response.message()}")
                    onFinished.onFailure(Throwable(response.message()))
                }
            }

            override fun onFailure(call: Call<List<Resulatdos>>, t: Throwable) {
                Log.e(TAG, "Error al llamar a la API: ${t.message}")
                onFinished.onFailure(t)
            }
        })
    }

}

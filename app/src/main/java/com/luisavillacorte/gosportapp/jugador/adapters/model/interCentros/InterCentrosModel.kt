package com.luisavillacorte.gosportapp.jugador.adapters.model.interCentros

import android.util.Log
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.homeCampeonatosService.HomeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class InterCentrosModel {

    private val apiService = RetrofitInstance.createService(HomeApiService::class.java)

    fun getPartidosJugados(equipoId: String, callback: (List<Partidos>?, String?) -> Unit) {
        apiService.getPartidosJugados(equipoId).enqueue(object : Callback<List<Partidos>> {
            override fun onResponse(call: Call<List<Partidos>>, response: Response<List<Partidos>>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, "Error: ${response.code()} - ${response.message()}")
                    Log.e("API_ERROR", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Partidos>>, t: Throwable) {

                callback(null, t.message)
                Log.d("API_ERROR", "onFailure: ${t.message}")
            }
        })
    }
}

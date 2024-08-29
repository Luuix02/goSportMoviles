package com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados

import com.luisavillacorte.gosportapp.planillero.adpaters.api.verEquipos.ApiServiceEquipos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EquiposAsignadosPresenter (
    private val view: EquiposAsignadosContract.View,
    private val equiposAsignadosService: ApiServiceEquipos
    ): EquiposAsignadosContract.Presenter{
        override fun obtenerEquiposAsignados(idFase: String) {
            val call = equiposAsignadosService.equiposAsignados( idFase)
            call.enqueue(object : Callback<List<Vs>> {
                override fun onResponse(call: Call<List<Vs>>, response: Response<List<Vs>>) {
                    if (response.isSuccessful) {
                        val equiposAsignados = response.body()
                        if (equiposAsignados != null) {
                            view.onEquposRecibidos(equiposAsignados)
                        }
                    } else {
                        view.error("Failed to retrieve data")
                    }
                }
                override fun onFailure(call: Call<List<Vs>>, t: Throwable) {
                    view.error(t.message?:"Error interno")
                }
            })
        }
}
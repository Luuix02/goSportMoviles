package com.luisavillacorte.gosportapp.planillero.adpaters.model.IntercentrosAgregarResultadosView

import com.luisavillacorte.gosportapp.planillero.adpaters.api.AgregarResultadosInter.ApiServiceAgregarResultadosIntercentros
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.EquiposIntercentrosAsignados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgregarResultadosIntercentrosPresenter (
    private val view: ContractAgergarResultadosInter.View,
    private val intercentrosInteractor: ApiServiceAgregarResultadosIntercentros
): ContractAgergarResultadosInter.Presenter{
    override fun obtenerDatosIntercentros(idVs: String) {
        val call = intercentrosInteractor.detalleVsIntercentros(idVs)
        call.enqueue(object : Callback<EquiposIntercentrosAsignados> {
            override fun onResponse(call: Call<EquiposIntercentrosAsignados>, response: Response<EquiposIntercentrosAsignados>) {

                if (response.isSuccessful && response.body() != null) {
                    view.onIntercentrosRecibidos(response.body()!!)
                } else {
                    view.error("Error en la respuesta")
                }
            }

            override fun onFailure(call: Call<EquiposIntercentrosAsignados>, t: Throwable) {

                view.error("Error: ${t.message}")
            }
        })
    }

}
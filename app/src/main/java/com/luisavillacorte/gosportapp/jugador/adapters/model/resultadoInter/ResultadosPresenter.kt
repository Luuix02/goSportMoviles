package com.luisavillacorte.gosportapp.jugador.adapters.model.resultadoInter

import android.util.Log

class ResultadosPresenter(private val view: ResultadosContract.View, private val model: ResultadosContract.Model) : ResultadosContract.Presenter {

    private val TAG = "ResultadosPresenter"

    override fun getResultados(idCampeonato: String) {
        model.fetchResultados(idCampeonato, object : ResultadosContract.Model.OnFinishedListener {
            override fun onSuccess(resultados: List<Resulatdos>) {
                Log.d(TAG, "Resultados recibidos: $resultados")
                view.showResultados(resultados)
            }

            override fun onFailure(t: Throwable) {
                Log.e(TAG, "Error al obtener resultados: ${t.message}")
                view.showError(t.message ?: "Error desconocido")
            }
        })
    }
}

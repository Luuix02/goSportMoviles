package com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados

import com.google.gson.Gson
import com.luisavillacorte.gosportapp.planillero.adpaters.api.vsEquiposResultados.ApiServiceResultados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultadosPresenter ( private val view: ResultadosContract.View,
                            private val resultadosService: ApiServiceResultados
) : ResultadosContract.Presenter {

//    override fun obtenerResultados(idVs: String) {
//        val call = resultadosService.getResultados(idVs)
//        call.enqueue(object : Callback<Any> {
//            override fun onResponse(call: Call<Any>, response: Response<Any>) {
//                if (response.isSuccessful) {
//                    val body = response.body()
//                    when (body) {
//                        is Boolean -> {
//                            if (body) {
//                                view.error("Datos no disponibles para el ID proporcionado.")
//                            } else {
//                                view.error("El ID proporcionado no fue encontrado.")
//                            }
//                        }
//                        is Map<*, *> -> {
//                            val resultados = Gson().fromJson(Gson().toJson(body), Resultados::class.java)
//                            view.onResultadosRecibidos(resultados)
//                        }
//                        else -> {
//                            view.error("Formato de respuesta inesperado.")
//                        }
//                    }
//                } else {
//                    handleErrorResponse(response)
//                }
//            }
//
//            override fun onFailure(call: Call<Any>, t: Throwable) {
//                view.error("Error al recuperar datos: ${t.message}")
//            }
//        })
//    }

    private fun handleErrorResponse(response: Response<Any>) {
        val errorMessage = when (response.code()) {
            404 -> "Datos no encontrados"
            500 -> "Error del servidor"
            else -> "Error desconocido: ${response.code()}"
        }
        view.error(errorMessage)
    }

    override fun obtenerResultados(idVs: String) {
        val call = resultadosService.detalleVs(idVs)
        call.enqueue(object : Callback<Vs> {
            override fun onResponse(call: Call<Vs>, response: Response<Vs>) {

                if (response.isSuccessful && response.body() != null) {
                    view.onResultadosRecibidos(response.body()!!)
                } else {
                    view.error("Error en la respuesta")
                }
            }

            override fun onFailure(call: Call<Vs>, t: Throwable) {

                view.error("Error: ${t.message}")
            }
        })
    }

    override fun subirDatosResultados(resultados: Resultados) {
       val call = resultadosService.subirResultados(resultados);
        call.enqueue(object : Callback<Resultados> {
            override fun onResponse(call: Call<Resultados>, response: Response<Resultados>) {

                if (response.isSuccessful) {
                    view.messageExito("Datos subidos con exito!")
                } else {
                    view.error("Error en la respuesta")
                }
            }

            override fun onFailure(call: Call<Resultados>, t: Throwable) {

                view.error("Error: ${t.message}")
            }
        })
    }

}

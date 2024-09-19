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
                    view.error("Error en la respuesta POST: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Resultados>, t: Throwable) {

                view.error("Error: ${t.message}")
            }
        })
    }

    override fun obtenerDatosUsuario(idJugador: String) {
        val call = resultadosService.getDatosUsuario(idJugador)
        call.enqueue(object : Callback<UsuariosJugadorDestacado> {
            override fun onResponse(call: Call<UsuariosJugadorDestacado>, response: Response<UsuariosJugadorDestacado>) {
                if (response.isSuccessful && response.body() != null) {
                    view.onJugadorDestacadoRecibido(response.body()!!)
                } else {
                    view.error("Error al obtener detalles del jugador")
                }
            }

            override fun onFailure(call: Call<UsuariosJugadorDestacado>, t: Throwable) {
                view.error("Error: ${t.message}")
            }
        })
    }

    override fun obtenerCampeonato(idCampeonato: String) {
        val call = resultadosService.getCampeonato(idCampeonato)
        call.enqueue(object : Callback<CampeonatoGetNombre> {
            override fun onResponse(call: Call<CampeonatoGetNombre>, response: Response<CampeonatoGetNombre>) {
                if (response.isSuccessful && response.body() != null) {
                    view.onCampeonatoRecibido(response.body()!!)
                } else {
                    view.error("Error al obtener detalles del campeonato")
                }
            }

            override fun onFailure(call: Call<CampeonatoGetNombre>, t: Throwable) {
                view.error("Error: ${t.message}")
            }
        })
    }

    override fun subirJugadorDestacado(jugadoresDestacados: DatosJugadorDestacado) {
        val call = resultadosService.agregarJugadorDestacado(jugadoresDestacados)
        call.enqueue(object : Callback<DatosJugadorDestacado> {
            override fun onResponse(call: Call<DatosJugadorDestacado>, response: Response<DatosJugadorDestacado>) {
                if (response.isSuccessful) {
                    view.onJugadorDestacadoSubido("Jugador(es) destacado(s) subido(s) con éxito")
                } else {
                    view.error("Error al subir jugador destacado")
                }
            }

            override fun onFailure(call: Call<DatosJugadorDestacado>, t: Throwable) {
                view.error("Error: ${t.message}")
            }
        })
    }
    }



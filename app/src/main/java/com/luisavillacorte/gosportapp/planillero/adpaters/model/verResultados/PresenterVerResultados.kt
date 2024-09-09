package com.luisavillacorte.gosportapp.planillero.adpaters.model.verResultados

import com.google.gson.Gson
import com.luisavillacorte.gosportapp.planillero.adpaters.api.vsEquiposResultados.ApiServiceResultados
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Participantes
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.EquipoR
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Goles
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.InscripcionEquipos1
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.InscripcionEquipos2
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Participante
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.Resultados
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PresenterVerResultados(
    private val view: verResultadosContract.View,
    private val verResultadosService: ApiServiceResultados
) : verResultadosContract.Presenter {

    override fun obtenerVerResultados(idVs: String) {
        val call = verResultadosService.getResultados(idVs)
        call.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    when (body) {
                        is Boolean -> {
                            if (body) {
                                view.error("Datos no disponibles para el ID proporcionado.")
                            } else {
                                view.error("El ID proporcionado no fue encontrado.")
                            }
                        }
                        is Map<*, *> -> {
                            val jsonObject = JSONObject(Gson().toJson(body))

                            // Mapear datos para equipo1 y equipo2
                            val equipo1 = mapearEquipo(jsonObject.getJSONObject("equipo1"), "Equipo1")
                            val equipo2 = mapearEquipo2(jsonObject.getJSONObject("equipo2"), "Equipo2")

                            val resultados = Resultados(
                                //_id = jsonObject.getString("_id"),
                                equipo1 = equipo1,
                                equipo2 = equipo2,
                                IdVs = jsonObject.getString("IdVs"),
                                //IdFase = jsonObject.getString("IdFase"),
                                estadoPartido = jsonObject.getBoolean("estadoPartido")
                            )

                            view.onVerReultados(resultados)
                        }
                        else -> {
                            view.error("Formato de respuesta inesperado.")
                        }
                    }
                } else {
                    handleErrorResponse(response)
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                view.error("Error al recuperar datos: ${t.message}")
            }
        })
    }
    private fun mapearEquipo2(equipoJson: JSONObject, equipoKey: String): InscripcionEquipos2 {
        val equipoR = equipoJson.getJSONObject(equipoKey).run {
            EquipoR(
                _id = getString("_id"),
                nombreEquipo = getString("nombreEquipo"),
                nombreCapitan = getString("nombreCapitan"),
                contactoUno = getString("contactoUno"),
                contactoDos = getString("contactoDos"),
                jornada = getString("jornada"),
                cedula = getString("cedula"),
                imgLogo = getString("imgLogo"),
                estado = getBoolean("estado"),
                participantes = getJSONArray("participantes").let { arr ->
                    List(arr.length()) { index ->
                        arr.getJSONObject(index).let {
                            Participante(
                                _id = it.getString("_id"),
                                nombres = it.getString("nombreJugador"),
                                ficha = it.getInt("ficha"),
                                dorsal = it.getInt("dorsal")
                            )
                        }
                    }
                }
            )
        }

        return InscripcionEquipos2(
            Equipo2 = equipoR,
            tarjetasAmarillas = mapearTarjetas(equipoJson.getJSONArray("tarjetasAmarillas")),
            tarjetasRojas = mapearTarjetas(equipoJson.getJSONArray("tarjetasRojas")),
            goles = equipoJson.getJSONObject("goles").let { golesJson ->
                Goles(
                    marcador = golesJson.getInt("marcador"),
                    jugadorGoleador = golesJson.getJSONArray("jugadorGoleador").let { arr ->
                        List(arr.length()) { index ->
                            arr.getJSONObject(index).let {
                                Participante(
                                    _id = it.getString("_id"),
                                    nombres = it.getString("nombreJugador"),
                                    ficha = it.getInt("ficha"),
                                    dorsal = it.getInt("dorsal")
                                )
                            }
                        }
                    }
                )
            }
        )
    }

    private fun mapearEquipo(equipoJson: JSONObject, equipoKey: String): InscripcionEquipos1 {
        val equipoR = equipoJson.getJSONObject(equipoKey).run {
            EquipoR(
                _id = getString("_id"),
                nombreEquipo = getString("nombreEquipo"),
                nombreCapitan = getString("nombreCapitan"),
                contactoUno = getString("contactoUno"),
                contactoDos = getString("contactoDos"),
                jornada = getString("jornada"),
                cedula = getString("cedula"),
                imgLogo = getString("imgLogo"),
                estado = getBoolean("estado"),
                participantes = getJSONArray("participantes").let { arr ->
                    List(arr.length()) { index ->
                        arr.getJSONObject(index).let {
                            Participante(
                                _id = it.getString("_id"),
                                nombres = it.getString("nombreJugador"),
                                ficha = it.getInt("ficha"),
                                dorsal = it.getInt("dorsal")
                            )
                        }
                    }
                }
            )
        }

        return InscripcionEquipos1(
            Equipo1 = equipoR,
            tarjetasAmarillas = mapearTarjetas(equipoJson.getJSONArray("tarjetasAmarillas")),
            tarjetasRojas = mapearTarjetas(equipoJson.getJSONArray("tarjetasRojas")),
            goles = equipoJson.getJSONObject("goles").let { golesJson ->
                Goles(
                    marcador = golesJson.getInt("marcador"),
                    jugadorGoleador = golesJson.getJSONArray("jugadorGoleador").let { arr ->
                        List(arr.length()) { index ->
                            arr.getJSONObject(index).let {
                                Participante(
                                    _id = it.getString("_id"),
                                    nombres = it.getString("nombreJugador"),
                                    ficha = it.getInt("ficha"),
                                    dorsal = it.getInt("dorsal")
                                )
                            }
                        }
                    }
                )
            }
        )
    }

    private fun mapearTarjetas(tarjetasArray: JSONArray): List<Participante> {
        return List(tarjetasArray.length()) { index ->
            tarjetasArray.getJSONObject(index).let {
                Participante(
                    _id = it.getString("_id"),
                    nombres = it.getString("nombreJugador"),
                    ficha = it.getInt("ficha"),
                    dorsal = it.getInt("dorsal")
                )
            }
        }
    }

    private fun handleErrorResponse(response: Response<Any>) {
        val errorMessage = when (response.code()) {
            404 -> "Datos no encontrados"
            500 -> "Error del servidor"
            else -> "Error desconocido: ${response.code()}"
        }
        view.error(errorMessage)
    }
}
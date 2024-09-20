package com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import com.luisavillacorte.gosportapp.planillero.adpaters.api.verEquipos.ApiServiceEquipos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EquiposAsignadosPresenter (
    private val view: EquiposAsignadosContract.View,
    private val context: Context,
    private val equiposAsignadosService: ApiServiceEquipos
    ): EquiposAsignadosContract.Presenter{
    private val tokenManager = TokenManager(context)
        override fun obtenerEquiposAsignados(idPlanillero: String) {
            val call = equiposAsignadosService.equiposAsignados(idPlanillero)
            call.enqueue(object : Callback<List<Vs>> {
                override fun onResponse(call: Call<List<Vs>>, response: Response<List<Vs>>) {
                    if (response.isSuccessful) {
                        val equipos = response.body()
                        Log.d("PlanilleroFragment", "Equipos recibidos: ${equipos?.size}")
                        if (equipos != null && equipos.isNotEmpty()) {
                            // Enviar los equipos recibidos a la vista
                            view.onEquposRecibidos(equipos)
                        } else {
                            // Manejar el caso donde el array es vacío
                            view.error("No se encontraron equipos asignados")
                            Log.e("PlanilleroFragment", "Equipos recibidos es vacío o null")
                        }
                    } else {
                        view.error("Error al obtener equipos asignados")
                    }
                }

                override fun onFailure(call: Call<List<Vs>>, t: Throwable) {
                    view.error("Error de conexión: ${t.message}")
                    Log.e("PlanilleroFragment", "Error: ${t.message}")
                }
            })        }

    override fun obtenerEquiposIntercentros(id: String) {
        val call = equiposAsignadosService.equiposAsignadosIntercentros(id)
        call.enqueue(object : Callback<List<EquiposIntercentrosAsignados>> {
            override fun onResponse(call: Call<List<EquiposIntercentrosAsignados>>, response: Response<List<EquiposIntercentrosAsignados>>) {
                if (response.isSuccessful) {
                    val equipos = response.body()
                    Log.d("PlanilleroFragment", "Equipos recibidos: ${equipos?.size}")
                    if (equipos != null && equipos.isNotEmpty()) {
                        // Enviar los equipos recibidos a la vista
                        view.EquiposIntercentrosRecibidos(equipos)
                    } else {
                        // Manejar el caso donde el array es vacío
                        view.error("No se encontraron equipos asignados")
                        Log.e("PlanilleroFragment", "Equipos recibidos es vacío o null")
                    }
                } else {
                    view.error("Error al obtener equipos asignados")
                }
            }

            override fun onFailure(call: Call<List<EquiposIntercentrosAsignados>>, t: Throwable) {
                view.error("Error de conexión: ${t.message}")
                Log.e("PlanilleroFragment", "Error: ${t.message}")
            }
        })
    }

    fun saveIdentificaionPlanillero(context: Context, identificaion: String) {
        val sharedPreferences = context.getSharedPreferences("planillero", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_identificaion", identificaion)
        Log.d("planillero", "identificaion planillero: ${identificaion}")
        editor.apply()
    }
    fun saveUserId(context: Context, id: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("USER_ID", id)
        editor.apply()
    }

    override fun obtenerPerfilUsuario() {
        val token = tokenManager.getToken() ?: return view.error("Token no disponible")

        val call = equiposAsignadosService.getPerfilPlanillero("Bearer $token")
        call.enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(call: Call<PerfilUsuarioResponse>, response: Response<PerfilUsuarioResponse>) {
                if (response.isSuccessful) {
                    val perfil = response.body()
                    if (perfil != null) {
                        saveUserId(context,perfil.nombres);
                        // Guarda el ID del planillero en SharedPreferences
                        saveIdentificaionPlanillero(context, perfil.identificacion)
                        // Muestra el perfil del planillero en la vista
                        view.PerfilPlanillero(perfil)
                    }
                } else {
                    view.error("Error al obtener el perfil del planillero")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.error("Error de conexión: ${t.message}")
            }
        })
    }
    }
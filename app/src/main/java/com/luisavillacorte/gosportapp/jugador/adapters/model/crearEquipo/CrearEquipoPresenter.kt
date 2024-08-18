package com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.formCrearEquipoService.CrearEquipoApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearEquipoPresenter(

    private val view: CrearEquipoContract.View,
    private val context: Context,
    private val apiService: CrearEquipoApiService

) : CrearEquipoContract.Presenter {

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        // Realizar la búsqueda de jugadores con el identificador actual
        if (currentQuery.isNotEmpty()) {
            realizarBusqueda(currentQuery)
        } else {
            view.showJugadores(emptyList())
        }
    }

    private var currentQuery: String = ""

    private val tokenManager = TokenManager(context)
    private val TAG = "CrearEquipoPresenter"
    override fun getPerfilUsuario() {

        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        Log.d(TAG, "Token obtenido: $token")
        val call = apiService.obtenerPerfilUsuario("Bearer $token")
        call.enqueue(object : Callback<PerfilUsuarioResponse> {
            override fun onResponse(
                call: Call<PerfilUsuarioResponse>,
                response: Response<PerfilUsuarioResponse>
            ) {
                if (response.isSuccessful) {
                    val perfil = response.body()
                    if (perfil != null) {
                        view.showPerfilUsuario(perfil)
                    } else {
                        view.showError("Perfil de usuario vacío")
                    }
                } else {
                    view.showError("Error al obtener el perfil ${response.code()}: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PerfilUsuarioResponse>, t: Throwable) {
                view.showError(t.message ?: "Error desconocido")
            }
        })
    }

    override fun crearEquipo(equipo: Equipo) {
        if (validateEquipo(equipo)) {
            val call = apiService.crearEquipo(equipo)
            call.enqueue(object : Callback<CrearEquipoResponse> {
                override fun onResponse(
                    call: Call<CrearEquipoResponse>,
                    response: Response<CrearEquipoResponse>
                ) {
                    if (response.isSuccessful) {
                        view.showSuccess("Equipo creado exitosamente")
                    } else {
                        view.showError("Error al crear el equipo ${response.code()}: ${response.message()}")
                    }
                }


                override fun onFailure(call: Call<CrearEquipoResponse>, t: Throwable) {
                    view.showError(t.message ?: "Error desconocido")
                }
            })
        } else {
            view.showError("Por favor, complete todos los campos del equipo")
        }
    }

    private fun validateEquipo(equipo:Equipo): Boolean {
        return equipo.nombreEquipo.isNotEmpty() &&
                equipo.nombreCapitan.isNotEmpty() &&
                equipo.contactoUno.isNotEmpty() &&
                equipo.contactoDos.isNotEmpty() &&
                equipo.participantes.size >= 8
    }

    override fun buscarJugadores(identificacion: String) {

        view.showLoading(true)

        // Establecer la consulta actual
        currentQuery = identificacion

        // Eliminar cualquier callback anterior
        handler.removeCallbacks(searchRunnable)


        // Programar un nuevo callback con un retraso de 300 ms
        handler.postDelayed(searchRunnable, 300)
    }

    private fun realizarBusqueda(query: String) {
        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
        val call = apiService.buscarJugadoresPorIdent("Bearer $token", query)
        Log.d(TAG, "Token enviado: Bearer $token")
        Log.d(TAG, "Identificación enviada: $query")

        call.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {

                view.showLoading(false)

                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    users.forEach { user ->
                        Log.d("CrearEquipoPresenter", "Datos del usuario: $user")
                        Log.d("CrearEquipoPresenter", "Rol del usuario: ${user.rol.trim()}")
                    }
                    Log.d("CrearEquipoPresenter", "Usuarios recibidos: $users")
                    val jugador = users.filter { user -> user.rol.trim().equals("jugador", ignoreCase = true) }
                    Log.d("CrearEquipoPresenter", "Usuarios filtrados como jugadores: $jugador")
                    // Mapeamos la lista de User a Participante
                    val participantes = jugador.map { user ->
                        Participante(
                            nombreJugador = user.nombres,
                            ficha = user.ficha ?: "",  // Asumiendo que ficha puede ser null en User
                            dorsal = ""  // Puedes asignar un valor por defecto o manejarlo según tus necesidades
                        )
                    }
                    view.showJugadores(users)  // Asegúrate de pasar la lista de Participante
                } else {
                    view.showError("Error al buscar jugadores")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                view.showLoading(false)
                Log.e(TAG, "Error en la solicitud: ${t.message}")
//                view.showError("Error en la solicitud: ${t.message}")
            }
        })
    }


}
//    override fun buscarJugadores(identificacion: String) {
//        val token = tokenManager.getToken() ?: return view.showError("Token no disponible")
//        val call = apiService.buscarJugadoresPorIdent("Bearer $token", identificacion)
//        Log.d("CrearEquipoPresenter", "Token enviado: Bearer $token")
//        Log.d("CrearEquipoPresenter", "Identificación enviada: $identificacion")
//
//        call.enqueue(object : Callback<List<User>> {
//            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
//                if (response.isSuccessful) {
//                    val users = response.body() ?: emptyList()
//                    // Mapeamos la lista de User a Participante
//                    val participantes = users.map { user ->
//                        Participante(
//                            nombreJugador = user.nombres,
//                            ficha = user.ficha ?: "",  // Asumiendo que ficha puede ser null en User
//                            dorsal = ""  // Puedes asignar un valor por defecto o manejarlo según tus necesidades
//                        )
//                    }
//                    view.showJugadores(users)
//                } else {
//                    view.showError("Error al buscar jugadores")
//                }
//            }
//
//            override fun onFailure(call: Call<List<User>>, t: Throwable) {
//                Log.e(TAG, "Error en la solicitud: ${t.message}")
//                view.showError("Error en la solicitud: ${t.message}")
//            }
//        })
//    }

//    private fun convertirUsuariosAParticipantes(users: List<User>): List<User> {
//        return users.map { user ->
//            Participante(
//                nombres = user.nombres,
//                ficha = user.ficha,
//                dorsal = user.dorsal,
//                isSelected = false
//            )
//        }
//    }


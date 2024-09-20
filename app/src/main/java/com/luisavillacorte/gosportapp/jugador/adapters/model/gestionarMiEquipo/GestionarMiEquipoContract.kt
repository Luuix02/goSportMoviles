package com.luisavillacorte.gosportapp.jugador.adapters.model.gestionarMiEquipo

import android.os.Message
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.UploadResponse
import okhttp3.MultipartBody

interface GestionarMiEquipoContract {

    interface View {
        fun mostrarActualizacionExitosa(equipo: Equipo)
        fun showError(mensaje: String)
        fun showValidacionExitosa(idJugador: String)
        fun showJugadores(jugadores: List<User>)
        fun showLoading(isLoading: Boolean)
        fun mostrarActualizacionLogoExitosa(message: String?, url: String?)
        fun mostrarDatosEquipo(equipo: Equipo)
    }

    interface Presenter {
        fun actualizarEquipo(equipo: Equipo, equipoId: String)
        fun validarInscripcion(idJugador: String)
        fun realizarBusqueda(query: String)
        fun actualizarLogoEquipo(id: String, idLogo: String, file: MultipartBody.Part)
    }
}
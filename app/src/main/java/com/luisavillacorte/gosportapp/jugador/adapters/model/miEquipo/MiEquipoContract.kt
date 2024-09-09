package com.luisavillacorte.gosportapp.jugador.adapters.model.miEquipo

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager

interface MiEquipoContract {
    interface View {
        fun mostrarEquipo(equipo: Equipo)
        fun mostrarError(message: String)
        fun showPerfilUsuario(perfil: PerfilUsuarioResponse)
    }

    interface Presenter {
        fun validarInscripcionJugador(idJugador: String) // Método para validar si el jugador pertenece a un equipo
        fun getPerfilUsuario()
    }
}
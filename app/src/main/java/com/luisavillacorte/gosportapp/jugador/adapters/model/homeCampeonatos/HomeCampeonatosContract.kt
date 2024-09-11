package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse

interface HomeCampeonatosContract {

    interface View {
        fun showLoading()
        fun hideLoading()
        fun showCampeonatos(campeonato: List<Campeonatos>)
        fun showError(message: String)
        fun showInscripcionError(message: String)
        fun navigateToCrearEquipo()
        fun navigateToGestionarEquipo(equipo: Equipo)
        fun mostrarBotonGestionarEquipo()
        fun mostrarBotonCrearEquipo()
        fun mostrarMensajeSnackBar(message: String)
        fun showValidacionInscripcion(estaInscrito: Boolean, equipo: Equipo?)
        fun showSuccess(message: String)
        fun traernombre(perfil: PerfilUsuarioResponse)
    }

    interface Presenter {
        fun getCampeonatos()

        fun getPerfilUsuario()
        fun validarInscripcionJugador(idJugador: String)
        fun validarInscripcionEquipo(identificacion: String)
    }
}

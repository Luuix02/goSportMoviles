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
        fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse)
        fun showInscripcionError(message: String)
        fun navigateToCrearEquipo()
        fun navigateToGestionarEquipo(equipo: Equipo)
        fun mostrarBotonGestionarEquipo()
//        fun ocultarBotonGestionarEquipo()
        fun mostrarBotonCrearEquipo()
        fun mostrarMensajeSnackBar(message: String)
        fun showValidacionInscripcion(estaInscrito: Boolean, equipo: Equipo?)

    }

    interface Presenter {
        fun getCampeonatos()
        fun getPerfilUsuario()
        fun validarInscripcionJugador(idJugador: String)


    }
}
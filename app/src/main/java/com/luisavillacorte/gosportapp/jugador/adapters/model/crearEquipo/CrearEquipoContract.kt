package com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User

interface CrearEquipoContract {

    interface View {
        fun showLoading(isLoading: Boolean)
        //        fun hideLoading()
        fun showError(message: String)
        fun showSuccess(message: String)
        fun validateFields(): Boolean
        fun getSelectedPlayers(): List<User>
        fun showJugadores(jugadores: List<User>)
        fun showPerfilUsuario(perfil: PerfilUsuarioResponse)
//        fun onEquipoCreado(crearEquipoResponse: CrearEquipoResponse?)

    }

    interface Presenter {
        fun getPerfilUsuario()
        fun crearEquipo(equipo: Equipo)
        fun buscarJugadores(identificacion: String)

    }
}
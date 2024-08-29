package com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo

import android.net.Uri
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.User

interface CrearEquipoContract {

    interface View {
        fun showLoading(isLoading: Boolean)
        fun showError(message: String)
        fun showSuccess(message: String)
        fun validateFields(): Boolean
        fun getSelectedPlayers(): List<User>
        fun showJugadores(jugadores: List<User>)
        fun showPerfilUsuario(perfil: PerfilUsuarioResponse)

        fun onImageUploadSuccess(imageUrl: String?, imagePublicId: String?)

        fun showValidacionExitosa(idJugador: String)

    }

    interface Presenter {
        fun validarInscripcion(idJugador: String)
        fun getPerfilUsuario()
        fun crearEquipo(equipo: Equipo)
        fun buscarJugadores(identificacion: String)
        fun subirLogoEquipo(userId: String, uri: Uri, equipo: Equipo)

    }
}

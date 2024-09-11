package com.luisavillacorte.gosportapp.jugador.adapters.model.editarPerfil

import android.net.Uri
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse

interface EditarPerfilContract {
    interface View {
        fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse)
        fun showSuccess(message: String)
        fun showError(message: String)
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun getPerfilUsuario()
        fun actualizarPerfilUsuario(id: String, perfilUsuarioResponse: PerfilUsuarioResponse)
        fun subirFoto(uri: Uri, idUser: String)
    }
}

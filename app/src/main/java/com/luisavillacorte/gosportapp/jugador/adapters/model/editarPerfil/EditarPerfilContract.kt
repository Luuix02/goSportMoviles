package com.luisavillacorte.gosportapp.jugador.adapters.model.editarPerfil

import android.net.Uri
import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import okhttp3.MultipartBody

interface EditarPerfilContract {

    interface View {
        fun traernombre(perfilUsuarioResponse: PerfilUsuarioResponse)
        fun showSuccess(message: String)
        fun showError(message: String)
        fun showLoading()
        fun hideLoading()
        fun mostrarProgramas(programas: List<Programas>)  // Método para mostrar programas
    }

    interface Presenter {
        fun getPerfilUsuario()
        fun actualizarPerfilUsuario(userId: String, perfilUsuarioResponse: PerfilUsuarioResponse)
        fun subirFoto(uri: Uri, userId: String)
        fun actualizarFoto(uri: Uri, userId: String)
        fun eliminarFoto(userId: String)
        fun obtenerProgramas()  // Método para obtener programas
    }

    interface Model {
        fun fetchPerfilUsuario(callback: (PerfilUsuarioResponse?) -> Unit)
        fun updatePerfilUsuario(userId: String, perfilUsuarioResponse: PerfilUsuarioResponse, callback: (Boolean) -> Unit)
        fun uploadFoto(uri: Uri, userId: String, callback: (Boolean) -> Unit)
        fun updateFoto(uri: Uri, userId: String, callback: (Boolean) -> Unit)
        fun deleteFoto(userId: String, callback: (Boolean) -> Unit)
        fun fetchProgramas(callback: (List<Programas>?) -> Unit)  // Método para obtener programas
    }
}

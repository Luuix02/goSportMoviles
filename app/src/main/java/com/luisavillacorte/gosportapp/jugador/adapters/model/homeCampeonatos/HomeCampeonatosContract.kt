package com.luisavillacorte.gosportapp.jugador.adapters.model.homeCampeonatos

import com.luisavillacorte.gosportapp.jugador.adapters.model.auth.PerfilUsuarioResponse
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.Equipo
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscriptoRequest
import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.EquipoInscriptoResponse

import com.luisavillacorte.gosportapp.jugador.adapters.model.crearEquipo.ValidarInscripcionResponse
import retrofit2.Response

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
//        fun actualizarEstadoInscripcion(idCampeonato: String)
        fun showSuccess(message: String)
//        fun mostrarAlerta(mensaje: String)
//        fun mostrarModalConfirmacion(onConfirm: () -> Unit)
//        fun mostrarMensaje(mensaje: String)

//        fun showInscripcionDetails(equipos: List<CampeonatoInscripcion>)
//        fun mostrarMensaje(message: String)

//        fun mostrarInscripcionExito(inscripto: Inscripto)
//        fun mostrarErrorInscripcion(error: String)

//        fun navigateToCrearEquipoCampe()

    }

    interface Presenter {
        fun getCampeonatos()
        fun getPerfilUsuario()
        fun validarInscripcionJugador(idJugador: String)
        fun verificarEquipoEnCampeonato(identificacion: String, callback: (Boolean) -> Unit)
        fun inscribirEquipoEnCampeonato(idCampeonato: String)
//        fun validarInscripcionEquipo(identificacion: String)
//        fun setCampeonatoSeleccionado(campeonatos: Campeonatos)
//        fun redirigirCrearEquipo()
//        fun inscribirEquipoEnCampeonato()
//        fun mostrarMensaje(message: String)




    }
}
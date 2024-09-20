package com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados

import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Vs

interface ResultadosContract {
        interface View{

            fun onResultadosRecibidos(vs: Vs)
            fun error(error: String)
            fun messageExito (message:String)
            fun onJugadorDestacadoRecibido(jugador: UsuariosJugadorDestacado)
            fun onCampeonatoRecibido(campeonato: CampeonatoGetNombre)
            fun onJugadorDestacadoSubido(message: String)

        }
        interface Presenter{
            fun obtenerResultados(idVs: String)
            fun subirDatosResultados (resultados: Resultados)
            fun obtenerDatosUsuario(idJugador: String)
            fun obtenerCampeonato(idCampeonato: String)
            fun subirJugadorDestacado(jugadoresDestacados: DatosJugadorDestacado)

        }
    }
package com.luisavillacorte.gosportapp.planillero.helper

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Equipo
import com.luisavillacorte.gosportapp.planillero.adpaters.model.equiposAsignados.Participantes
import com.luisavillacorte.gosportapp.planillero.adpaters.model.resultadosEquiposAsignados.UsuariosJugadorDestacado


class VerTarjetas:ViewModel() {
    private val _jugadoresPorVs = MutableLiveData<MutableMap<String, MutableMap<String, Participantes>>>()
    val jugadoresPorVs: LiveData<MutableMap<String, MutableMap<String, Participantes>>> get() = _jugadoresPorVs
    var totalGolesEquipo1PorVs: MutableMap<String, Int> = mutableMapOf()
        private set
    var totalGolesEquipo2PorVs: MutableMap<String, Int> = mutableMapOf()
        private set

    private val _golesPorJugadorEquipo1PorVs = MutableLiveData<MutableMap<String, MutableMap<String, Int>>>()
    private val _golesPorJugadorEquipo2PorVs = MutableLiveData<MutableMap<String, MutableMap<String, Int>>>()
    val golesPorJugadorEquipo1PorVs: LiveData<MutableMap<String, MutableMap<String, Int>>> get() = _golesPorJugadorEquipo1PorVs
    val golesPorJugadorEquipo2PorVs: LiveData<MutableMap<String, MutableMap<String, Int>>> get() = _golesPorJugadorEquipo2PorVs


    private val _amarillasPorJugadorEquipo1 = MutableLiveData<MutableMap<String, MutableMap<String, Int>>>()
    private val _amarillasPorJugadorEquipo2 = MutableLiveData<MutableMap<String, MutableMap<String, Int>>>()
    private val _rojasPorJugadorEquipo1 = MutableLiveData<MutableMap<String, MutableMap<String, Int>>>()
    private val _rojasPorJugadorEquipo2 = MutableLiveData<MutableMap<String, MutableMap<String, Int>>>()
    val amarillasEquipo1: LiveData<MutableMap<String, MutableMap<String, Int>>> get() = _amarillasPorJugadorEquipo1
    val amarillasEquipo2: LiveData<MutableMap<String, MutableMap<String, Int>>> get() = _amarillasPorJugadorEquipo2
    val rojasEquipo1: LiveData<MutableMap<String, MutableMap<String, Int>>> get() = _rojasPorJugadorEquipo1
    val rojasEquipo2: LiveData<MutableMap<String, MutableMap<String, Int>>> get() = _rojasPorJugadorEquipo2

    var totalTarjetasAmarillasEquipo1: MutableMap<String, Int> = mutableMapOf()
        private set
    var totalTarjetasAmarillasEquipo2: MutableMap<String, Int> = mutableMapOf()
        private set

    var totalTarjetasRojasEquipo1: MutableMap<String, Int> = mutableMapOf()
        private set
    var totalTarjetasRojasEquipo2: MutableMap<String, Int> = mutableMapOf()

    init {

        _jugadoresPorVs.value = mutableMapOf()
        _golesPorJugadorEquipo1PorVs.value = mutableMapOf()
        _golesPorJugadorEquipo2PorVs.value = mutableMapOf()
        _amarillasPorJugadorEquipo1.value = mutableMapOf()
        _amarillasPorJugadorEquipo2.value = mutableMapOf()
        _rojasPorJugadorEquipo1.value = mutableMapOf()
        _rojasPorJugadorEquipo2.value = mutableMapOf()
    }
    private val _jugadoresDestacados = MutableLiveData<MutableMap<String, MutableSet<String>>>()
    val jugadoresDestacados: LiveData<MutableMap<String, MutableSet<String>>> get() = _jugadoresDestacados

    init {
        _jugadoresDestacados.value = mutableMapOf()
    }

    fun agregarJugadorDestacado(idVs: String, jugadorId: String) {
        val jugadoresDestacadosMap = _jugadoresDestacados.value ?: mutableMapOf()
        val listaDestacados = jugadoresDestacadosMap[idVs] ?: mutableSetOf()

        if (listaDestacados.add(jugadorId)) {
            jugadoresDestacadosMap[idVs] = listaDestacados
            _jugadoresDestacados.value = jugadoresDestacadosMap
        }
    }
    private val equipos: List<Equipo> = listOf()
    private fun obtenerEquipoPorId(idEquipo: String): Equipo? {
        return equipos.find { it.id == idEquipo } // Devuelve el objeto equipo correspondiente
    }
    fun ganador(idVs: String): Equipo? {
        val golesEquipo1 = obtenerTotalGolesEquipo1(idVs)
        val golesEquipo2 = obtenerTotalGolesEquipo2(idVs)

        return if (golesEquipo1 > golesEquipo2) {
            obtenerEquipoPorId("idEquipo1") // Reemplazar con el ID real del equipo 1
        } else {
            obtenerEquipoPorId("idEquipo2") // Reemplazar con el ID real del equipo 2
        }
    }

    // Función para determinar el perdedor basado en goles y retornar el objeto del equipo
    fun perdedor(idVs: String): Equipo? {
        val golesEquipo1 = obtenerTotalGolesEquipo1(idVs)
        val golesEquipo2 = obtenerTotalGolesEquipo2(idVs)

        return if (golesEquipo1 < golesEquipo2) {
            obtenerEquipoPorId("idEquipo1") // Reemplazar con el ID real del equipo 1
        } else {
            obtenerEquipoPorId("idEquipo2") // Reemplazar con el ID real del equipo 2
        }
    }


    fun quitarJugadorDestacado(idVs: String, jugadorId: String) {
        val jugadoresDestacadosMap = _jugadoresDestacados.value ?: mutableMapOf()
        jugadoresDestacadosMap[idVs]?.remove(jugadorId)
        _jugadoresDestacados.value = jugadoresDestacadosMap
    }


    fun obtenerTotalGolesEquipo1(idVs: String): Int {
        return _golesPorJugadorEquipo1PorVs.value?.get(idVs)?.values?.sum() ?: 0
    }

    fun obtenerTotalGolesEquipo2(idVs: String): Int {
        return _golesPorJugadorEquipo2PorVs.value?.get(idVs)?.values?.sum() ?: 0
    }

    fun aumentarGol(idVs: String, jugadorId: String, equipo: Int) {
        val golesPorJugador = if (equipo == 1) _golesPorJugadorEquipo1PorVs else _golesPorJugadorEquipo2PorVs
        val goles = golesPorJugador.value?.get(idVs) ?: mutableMapOf()
        goles[jugadorId] = (goles[jugadorId] ?: 0) + 1
        golesPorJugador.value = golesPorJugador.value?.apply { put(idVs, goles) } ?: mutableMapOf(idVs to goles)

        if (equipo == 1) {
            totalGolesEquipo1PorVs[idVs] = (totalGolesEquipo1PorVs[idVs] ?: 0) + 1
        } else {
            totalGolesEquipo2PorVs[idVs] = (totalGolesEquipo2PorVs[idVs] ?: 0) + 1
        }

        val jugador = _jugadoresPorVs.value?.get(idVs)?.get(jugadorId)
        jugador?.let {
            Log.d("TAG", "Gol anotado por: ID=${it._id}, Nombre=${it.nombres}, Dorsal=${it.dorsal}, Ficha=${it.ficha}")
        }
    }

    fun disminuirGol(idVs: String ,jugadorId: String, equipo: Int):Boolean {
        val golesPorJugador = if (equipo == 1) _golesPorJugadorEquipo1PorVs else _golesPorJugadorEquipo2PorVs
        val goles = golesPorJugador.value?.get(idVs) ?: mutableMapOf()

        val golesJugador = goles[jugadorId] ?: 0

        if (golesJugador > 0) {
            goles[jugadorId] = golesJugador - 1
            golesPorJugador.value = golesPorJugador.value?.apply { put(idVs, goles) } ?: mutableMapOf(idVs to goles)

            if (equipo == 1) {
                totalGolesEquipo1PorVs[idVs] = (totalGolesEquipo1PorVs[idVs] ?: 0) - 1
            } else {
                totalGolesEquipo2PorVs[idVs] = (totalGolesEquipo2PorVs[idVs] ?: 0) - 1
            }

            val jugador = _jugadoresPorVs.value?.get(idVs)?.get(jugadorId)
            jugador?.let {
                Log.d("TAG", "Gol descontado por: ID=${it._id}, Nombre=${it.nombres}, Dorsal=${it.dorsal}, Ficha=${it.ficha}")
            }
            return true
        } else {
            Log.d("TAG", "No se puede disminuir el gol del jugador $jugadorId porque ya tiene 0 goles.")
            return false
        }
    }

    fun getGoles(idVs: String,jugadorId: String, equipo: Int): Int {
        return if (equipo == 1) {
            golesPorJugadorEquipo1PorVs.value?.get(idVs)?.get(jugadorId) ?: 0
        } else {
            golesPorJugadorEquipo2PorVs.value?.get(idVs)?.get(jugadorId) ?: 0
        }
    }
    fun obtenerTotalAmarillasEquipo1(idVs: String):Int{
        return _amarillasPorJugadorEquipo1.value?.get(idVs)?.values?.sum() ?: 0
    }
    fun obtenerTotalAmarillasEquipo2(idVs: String):Int{
        return _amarillasPorJugadorEquipo2.value?.get(idVs)?.values?.sum() ?: 0
    }
    fun obtenerTotalRojasEquipo1(idVs: String):Int{
        return _rojasPorJugadorEquipo1.value?.get(idVs)?.values?.sum() ?: 0
    }
    fun obtenerTotalRojasEquipo2(idVs: String):Int{
        return _rojasPorJugadorEquipo2.value?.get(idVs)?.values?.sum() ?: 0
    }
    fun aumentarTarjetaAmarilla(idVs:String, jugadorId: String, equipo: Int) {
        val amarillasByJugador = if (equipo == 1) _amarillasPorJugadorEquipo1 else _amarillasPorJugadorEquipo2
        val amarillas = amarillasByJugador.value?.get(idVs) ?: mutableMapOf()

        if ((amarillas[jugadorId] ?: 0) < 2) {
            amarillas[jugadorId] = (amarillas[jugadorId] ?: 0) + 1
            amarillasByJugador.value = amarillasByJugador.value?.apply { put(idVs, amarillas) } ?: mutableMapOf(idVs to amarillas)

            if (equipo == 1) {
                totalTarjetasAmarillasEquipo1[idVs] = (totalTarjetasAmarillasEquipo1[idVs] ?: 0) + 1
            } else {
                totalTarjetasAmarillasEquipo2[idVs] = (totalTarjetasAmarillasEquipo2[idVs] ?: 0) + 1
            }

            // Acceder a la información del jugador
            val jugador = _jugadoresPorVs.value?.get(idVs)?.get(jugadorId)
            jugador?.let {
                Log.d("TAG", "Tarjeta amarilla anotada por: ID=${it._id}, Nombre=${it.nombres}, Dorsal=${it.dorsal}, Ficha=${it.ficha}")
            }
        } else {
            Log.d("TAG", "No se puede anotar T.A a ${jugadorId} porque ya tiene 2 tarjetas amarillas.")
        }

    }

    fun disminuirTarjetaAmarilla(idVs: String, jugadorId: String, equipo: Int):Boolean {
        val amarillasByJugador = if (equipo == 1) _amarillasPorJugadorEquipo1 else _amarillasPorJugadorEquipo2
        val amarillas = amarillasByJugador.value?.get(idVs) ?: mutableMapOf()

        val amarillasJugador = amarillas[jugadorId] ?: 0

        if (amarillasJugador > 0){
            amarillas[jugadorId] = amarillasJugador - 1
            amarillasByJugador.value = amarillasByJugador.value?.apply { put(idVs , amarillas) } ?: mutableMapOf(idVs to amarillas);

            if (equipo == 1) {
                totalTarjetasAmarillasEquipo1[idVs] = (totalTarjetasAmarillasEquipo1[idVs] ?: 0) - 1
            }
            else {
                totalTarjetasAmarillasEquipo2[idVs] = (totalTarjetasAmarillasEquipo2[idVs] ?: 0) - 1
            }
            val jugador = _jugadoresPorVs.value?.get(idVs)?.get(jugadorId);
            jugador?.let {
                Log.d("TAG", "Tarjeta amarilla descontada por: ID=${it._id}, Nombre=${it.nombres}, Dorsal=${it.dorsal}, Ficha=${it.ficha}")
            }
            return true
        }else{
            Log.d("TAG", "No se puede disminuir T.A a ${jugadorId} porque ya tiene 0 tarjetas.")
            return false
        }
    }


    fun getTarjetasAmarillas(idVs: String,jugadorId: String, equipo: Int): Int {
        return if (equipo == 1){
            amarillasEquipo1.value?.get(idVs)?.get(jugadorId) ?: 0
        }else{
            amarillasEquipo2.value?.get(idVs)?.get(jugadorId) ?: 0
        }
    }

    fun aumentarTarjetaRoja(idVs:String, jugadorId: String, equipo: Int) {

        val rojasByjugador = if (equipo == 1) _rojasPorJugadorEquipo1 else _rojasPorJugadorEquipo2
        val rojas = rojasByjugador.value?.get(idVs) ?: mutableMapOf()

        if ((rojas[jugadorId] ?: 0) < 1) {
            rojas[jugadorId] = (rojas[jugadorId] ?: 0) + 1
            rojasByjugador.value = rojasByjugador.value?.apply { put(idVs, rojas) } ?: mutableMapOf(idVs to rojas)

            if (equipo == 1) {
                totalTarjetasRojasEquipo1[idVs] = (totalTarjetasRojasEquipo1[idVs] ?: 0) + 1
            } else {
                totalTarjetasRojasEquipo2[idVs] = (totalTarjetasRojasEquipo2[idVs] ?: 0) + 1
            }

            val jugador = _jugadoresPorVs.value?.get(idVs)?.get(jugadorId)
            jugador?.let {
                Log.d("TAG", "Tarjeta roja anotada por: ID=${it._id}, Nombre=${it.nombres}, Dorsal=${it.dorsal}, Ficha=${it.ficha}")
            }
        } else {
            Log.d("TAG", "No se puede anotar T.R a ${jugadorId} porque ya tiene 1 tarjeta roja.")
        }

    }

    fun disminuirTarjetaRoja(idVs:String, jugadorId: String,equipo: Int): Boolean {
        val rojasByJugador = if (equipo == 1) _rojasPorJugadorEquipo1 else _rojasPorJugadorEquipo2
        val rojas = rojasByJugador.value?.get(idVs) ?: mutableMapOf();

        val rojasJugador = rojas[jugadorId] ?: 0
        if (rojasJugador > 0){
            rojas[jugadorId] = rojasJugador - 1
            rojasByJugador.value = rojasByJugador.value?.apply { put(idVs, rojas) } ?: mutableMapOf(idVs to rojas)

            if (equipo == 1) {
                totalTarjetasRojasEquipo1[idVs] = (totalTarjetasRojasEquipo1[idVs] ?: 0)+1;
            } else {
                totalTarjetasRojasEquipo2[idVs] = (totalTarjetasRojasEquipo2[idVs] ?: 0)+1
            }
            val jugador = _jugadoresPorVs.value?.get(idVs)?.get(jugadorId);
            jugador?.let {
                Log.d("TAG", "Tarjeta roja descontada por: ID=${it._id}, Nombre=${it.nombres}, Dorsal=${it.dorsal}, Ficha=${it.ficha}")
            }
            return true
        }else{
            Log.d("TAG", "No se puede disminuir T.R a ${jugadorId} porque ya tiene 0 tarjetas.")
            return false
        }
    }

    fun getTarjetasRojas(idVs: String, jugadorId: String, equipo: Int): Int {
        return if (equipo == 1){
            rojasEquipo1.value?.get(idVs)?.get(jugadorId) ?: 0
        }else{
            rojasEquipo2.value?.get(idVs)?.get(jugadorId) ?: 0
        }
    }


}
package com.luisavillacorte.gosportapp.jugador.adapters.repository

import com.luisavillacorte.gosportapp.jugador.adapters.storage.TokenManager

class TokenRepository ( private val tokenManager: TokenManager) {

    fun saveToken(token: String) {
        tokenManager.saveToken(token)
    }

    fun getToken(): String? {
        return  tokenManager.getToken()
    }
}

package com.luisavillacorte.gosportapp.jugador.adapters.model.auth

data class AuthResponse(

    val success: Boolean,
    val message: String?,
    val token: String?,
    val user: User

) {
    override fun toString(): String {
        return "AuthResponse(success=$success, message=$message, token=$token, user=$user)"
    }

}
package com.luisavillacorte.gosportapp.jugador.util

import android.util.Patterns

object ValidationUser {
    fun fieldsComplete(vararg fields: String): Boolean{
        return fields.none() { it.isBlank()}
    }

    fun validateEmail(correo: String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(correo).matches()
    }

    fun validatePassword(contrasena: String): Boolean{
        return contrasena.length >= 6 && contrasena.any { it.isDigit() } && contrasena.any { !it.isLetterOrDigit() }
    }

    fun validatePhone(telefono: String): Boolean{
        return Patterns.PHONE.matcher(telefono).matches()
    }

    fun passwordConfimated(contrasena: String, confirmarContrasena: String ): Boolean{
        return  contrasena == confirmarContrasena
    }
}
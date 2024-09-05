package com.luisavillacorte.gosportapp.jugador.adapters.model.auth

import com.google.gson.annotations.SerializedName

data class PerfilUsuarioResponse(
    @SerializedName("_id") val id: String,
    val nombres: String,
    val telefono: String,
    val correo: String,
    //val contrasena: String,  // Contraseña encriptada
    val genero: String,
    //@SerializedName("url_foto") val urlFoto: String,
    val identificacion: String,
    //val rol: String?,
    val ficha: String?,
    //val finFicha: String,
    val jornada: String,
    val programa: String

)

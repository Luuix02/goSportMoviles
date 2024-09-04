package com.luisavillacorte.gosportapp.jugador.adapters.model.auth

import com.google.gson.annotations.SerializedName

data class PerfilUsuarioResponse(

    val estado: Boolean,
    @SerializedName("_id") val id: String,
    val nombres: String,
    val telefono: String,
    val correo: String,
    val ficha:String,
    val jornada:String,
    val contrasena: String,
    val identificacion: String,
    val rol: String,
    @SerializedName("url_foto") val url_foto: String
)

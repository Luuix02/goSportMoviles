package com.luisavillacorte.gosportapp.jugador.adapters.model.auth

import com.google.gson.annotations.SerializedName

data class PerfilUsuarioResponse(
    @SerializedName("_id") val id: String,
    val nombres: String,
    val telefono: String,
    val ficha: String,
    val correo: String,

    val public_id: String?,
    val url_foto: String? ,
    val contrasena: String,

    //val contrasena: String,  // Contraseña encriptada

    //@SerializedName("url_foto") val urlFoto: String,

    val identificacion: String,

    val rol: String,
    @SerializedName("esCapitan") val esCapitan: Boolean,

//    val ficha: String?,
    val jornada: String,
    val programa: String

)

data class CloudinaryResponse(
    @SerializedName("public_id") val publicId: String,
    @SerializedName("url_foto") val urlFoto: String

)

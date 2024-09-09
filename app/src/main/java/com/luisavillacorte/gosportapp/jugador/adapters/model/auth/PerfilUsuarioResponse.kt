package com.luisavillacorte.gosportapp.jugador.adapters.model.auth

import com.google.gson.annotations.SerializedName

data class PerfilUsuarioResponse(

    val estado: Boolean,
    @SerializedName("_id") val id: String,
    val nombres: String,
    val telefono: String,
    val correo: String,
    val public_id: String?,
    val url_foto: String? ,
    val contrasena: String,
    val identificacion: String,
    val rol: String
)

data class CloudinaryResponse(
    @SerializedName("public_id") val publicId: String,
    @SerializedName("url_foto") val urlFoto: String
)

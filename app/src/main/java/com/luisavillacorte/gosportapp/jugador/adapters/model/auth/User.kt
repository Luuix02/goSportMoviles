package com.luisavillacorte.gosportapp.jugador.adapters.model.auth

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class User(

    @SerializedName("_id") val id: String,
    val nombres: String,
    val telefono: String,
    val identificacion: String,
    val ficha: String,
    val programa: String? = null,
    val finFicha: Date? = null,
    val jornada: String? = null,
    val correo: String,
    val contrasena: String? = null,
    val rol: String,
    var dorsal: String,
    var isSelected: Boolean = false,
    @SerializedName("esCapitan") val esCapitan: Boolean = false

) : Parcelable{
    override fun toString(): String {
        return "User(nombres='$nombres', ficha='$ficha', rol='$rol')"
    }
}


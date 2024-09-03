package com.luisavillacorte.gosportapp.jugador.adapters.model.perfil

import android.os.Message

interface ProfileContract {
    interface View{
        fun showProfile(profile: PlayerProfile)
        fun showError(message: String)
    }
    interface Presenter{
        fun loadProfile()
    }
}
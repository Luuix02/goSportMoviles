package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.editarPerfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.luisavillacorte.gosportapp.R

class Editarperfil : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_editarperfil, container, false)
    }
}

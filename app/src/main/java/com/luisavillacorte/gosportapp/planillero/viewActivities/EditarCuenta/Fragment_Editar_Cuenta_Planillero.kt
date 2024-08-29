package com.luisavillacorte.gosportapp.planillero.viewActivities.EditarCuenta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.luisavillacorte.gosportapp.R

class Fragment_Editar_Cuenta_Planillero : Fragment() {

    private lateinit var BotonVolver : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment__editar__cuenta__planillero, container, false)
    }


}
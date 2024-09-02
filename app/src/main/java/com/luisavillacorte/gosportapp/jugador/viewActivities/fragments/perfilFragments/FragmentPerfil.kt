package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.perfilFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.editarPerfil.Editarperfil

class FragmentPerfil : Fragment() {
    private lateinit var editar: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        // Encuentra el TextView para la acción de editar
        editar = view.findViewById(R.id.editar_perfil)

        // Configura el listener del clic
        editar.setOnClickListener {
            editarperfil()
        }

        return view
    }

    private fun editarperfil() {
        // Reemplaza el fragmento actual con el nuevo fragmento
        val fragment = Editarperfil()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_perfil, fragment)
            .addToBackStack(null)
            .commit()
    }
}

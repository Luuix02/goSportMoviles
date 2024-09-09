package com.luisavillacorte.gosportapp.jugador.viewActivities.activities.navegacioMenuApp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.luisavillacorte.gosportapp.FragmentMiEquipo
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.gestionarMiEquipoFragments.FragmentGestionarMiEquipo
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.misPartidosFragments.FragmentMisPartidos
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.homeFragments.FragmentHome
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.perfilFragments.FragmentPerfil

class NavegacionMenuApp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_navegacion_menu_app)


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_inicio -> selectedFragment = FragmentHome()

                R.id.navigation_equipo -> selectedFragment = FragmentMiEquipo()

                R.id.misPartidos -> selectedFragment = FragmentMisPartidos()
                R.id.navigation_perfil -> selectedFragment = FragmentPerfil()
            }
            selectedFragment?.let {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, it).commit()
            }
            true
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentHome()).commit()
            bottomNavigationView.selectedItemId = R.id.navigation_inicio
        }
    }


}
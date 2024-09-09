package com.luisavillacorte.gosportapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.luisavillacorte.gosportapp.jugador.viewActivities.MainActivity
import com.luisavillacorte.gosportapp.jugador.viewActivities.activities.activitiesAuth.ActivityLogin
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.homeFragments.FragmentHome

class ActivityLanding : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_landing)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        setupNavigation(navView)
        updateMenuTitle(navView, false)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FragmentLanding()).commit()
            navView.selectedItemId = R.id.navigation_inicio
        }

    }

    //        val menu : Menu = navView.menu
//        menu.findItem(R.id.navigation_equipo).isEnabled = true
//        menu.findItem(R.id.planilla).isEnabled = true
//        navView.menu.findItem(R.id.navigation_equipo).icon = ContextCompat.getDrawable(this, R.drawable.ic_equipo)
//        navView.menu.findItem(R.id.planilla).icon = ContextCompat.getDrawable(this, R.drawable.ic_mis_partidos)
    private fun updateMenuTitle(navView: BottomNavigationView, isUserLoggedIn: Boolean) {
        val menu = navView.menu
        val profileItem = menu.findItem(R.id.navigation_perfil)
        profileItem.title = getString(if (isUserLoggedIn) R.string.profile else R.string.login)
    }

    private fun setupNavigation(navView: BottomNavigationView) {
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_inicio -> {
                    val fragment = FragmentLanding()
                    supportFragmentManager.beginTransaction()
                        //cambio fragment_container
                        .replace(R.id.fragment_container, fragment)
                        .commit()
                    true
                }

                R.id.navigation_perfil -> {
                    startActivity(Intent(this, ActivityLogin::class.java))
                    true
                }

                else -> false
            }

        }

    }


}




package com.luisavillacorte.gosportapp.jugador.viewActivities.activities.principalMain

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.luisavillacorte.gosportapp.jugador.viewActivities.activities.activitiesAuth.ActivityLogin
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.viewActivities.activities.activitiesAuth.RegisterActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnRegister = findViewById<Button>(R.id.btn_register)


        btnLogin.setOnClickListener {
            val loginIntent = Intent(this, ActivityLogin::class.java)
            startActivity(loginIntent)
        }


        btnRegister.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

    }
    }

package com.luisavillacorte.gosportapp.jugador.viewActivities.activities.activitiesAuth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.jugador.adapters.model.register.RegisterContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.register.RegisterPresenter
import com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.registerFragments.RegisterOneFragment

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    lateinit var presenter: RegisterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        presenter = RegisterPresenter()
        presenter.attachView(this)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                //fragment_container
                .replace(R.id.fragment_container, RegisterOneFragment())
                .commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearSavedData()
    }

//    override fun onStop() {
//        super.onStop()
//        clearSavedData()
//    }

    override fun showFirstSection() {

    }

    override fun showSecondSection() {

    }

    override fun showSuccess() {
        Log.d("RegisterActivity", "Registro exitoso")
        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
        clearSavedData()
        startActivity(Intent(this, ActivityLogin::class.java))
        finish()
    }

    override fun showError(message: String) {
        Log.e("RegisterActivity", "Error: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun saveFirstSectionData(nombres: String, identificacion: String, telefono: String, jornada: String, programa: String ){
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()){
            putString("nombres", nombres)
            putString("identificacion", identificacion)
            putString("telefono", telefono)
            putString("jornada", jornada)
            putString("programa", programa)
            apply()

        }
    }

    fun saveSecondSectionData(ficha: String, finFicha: String,correo: String, contrasena: String){
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()){
            putString("ficha", ficha)
            putString("finFicha", finFicha)
            putString("correo", correo)
            putString("contrasena", contrasena)
            apply()

        }
    }

    fun getSavedData(): Map<String, String?>{
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        return mapOf(
            "nombres" to sharedPreferences.getString("nombres", null),
            "identificacion" to sharedPreferences.getString("identificacion", null),
            "telefono" to sharedPreferences.getString("telefono", null),
            "jornada" to sharedPreferences.getString("jornada", null),
            "programa" to sharedPreferences.getString("programa", null),
            "ficha" to sharedPreferences.getString("ficha", null),
            "finFicha" to sharedPreferences.getString("finFicha", null),
            "correo" to sharedPreferences.getString("correo", null),
            "contrasena" to sharedPreferences.getString("contrasena", null),
            "confirmarContrasena" to sharedPreferences.getString("confirmarContrasena", null)
        )

    }

    fun clearSavedData(){
        val sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        with(sharedPreferences.edit()){
            clear()
            apply()
        }
    }

    }

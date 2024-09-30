package com.luisavillacorte.gosportapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.recuperarContrasena.RecuperarContrasenaApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.RecuperarContrasenaContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.RecuperarContrasenaPresenter
import com.luisavillacorte.gosportapp.jugador.viewActivities.activities.activitiesAuth.ActivityLogin

class ActivityCambiarContrasena : AppCompatActivity(), RecuperarContrasenaContract.CambiarContrasenaView {
    private lateinit var presenter: RecuperarContrasenaContract.Presenter
    private lateinit var nuevaContrasenaInput: EditText
    private lateinit var btnRestablecer: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_contrasena)

        nuevaContrasenaInput = findViewById(R.id.correoRecuperar)
        btnRestablecer = findViewById(R.id.btnRestablecerClave)

        val apiService = RetrofitInstance.createService(RecuperarContrasenaApiService::class.java)
        presenter = RecuperarContrasenaPresenter(apiService, cambiarContrasenaView = this)

        val correo = intent.getStringExtra("correo")
        Log.d("CambiarContrasena", "Correo recibido: $correo")

        if (correo.isNullOrEmpty()) {
            showError("No se encontró el correo para cambiar la contraseña.")
            return
        }

        btnRestablecer.setOnClickListener {
            val nuevaContrasena = nuevaContrasenaInput.text.toString()
            Log.d("CambiarContrasena", "Nueva contraseña ingresada: $nuevaContrasena")

            if (nuevaContrasena.isNotEmpty()) {
                presenter.cambiarContrasena(correo, nuevaContrasena)
            } else {
                showError("Por favor ingresa una nueva contraseña.")
            }
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.e("CambiarContrasena", "Error: $message")
    }

    override fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        Log.d("CambiarContrasena", "Éxito: $message")

    }
    override fun showLoading() {

    }
    override fun hideLoading() {

    }
    override fun navigateToLoginScreen() {
        val intent = Intent(this, ActivityLogin::class.java)
        startActivity(intent)
        finish()
    }
}
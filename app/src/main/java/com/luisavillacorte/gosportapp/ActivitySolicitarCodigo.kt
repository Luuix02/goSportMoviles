package com.luisavillacorte.gosportapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.recuperarContrasena.RecuperarContrasenaApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.RecuperarContrasenaContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.RecuperarContrasenaPresenter

class ActivitySolicitarCodigo : AppCompatActivity(), RecuperarContrasenaContract.SolicitarCodigoView {

    private lateinit var presenter: RecuperarContrasenaContract.Presenter
    private lateinit var correoInput: EditText
    private lateinit var botonEnviarCodigo: Button
    private lateinit var contenedor: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_solicitar_codigo)

        correoInput = findViewById(R.id.correoRecuperar)
        botonEnviarCodigo = findViewById(R.id.btnRecuperar)
        contenedor = findViewById(R.id.contenedor)

        val apiService = RetrofitInstance.createService(RecuperarContrasenaApiService::class.java)
        presenter = RecuperarContrasenaPresenter(apiService, solicitarCodigoView = this)


        botonEnviarCodigo.setOnClickListener {
            val correo = correoInput.text.toString()
            Log.d("ActivitySolicitarCodigo", "Correo ingresado: $correo")
            if (correo.isNotEmpty()) {
                presenter.solicitarCodigo(correo)
            } else {
                showError("Por favor ingrese un correo electrónico.")
            }
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun showEmailInput() {
    }

    override fun showVerificationInput() {

    }

    override fun showNewPasswordInput() {

    }
    override fun navigateToVerificationScreen(correo: String) {
        Log.d("ActivitySolicitarCodigo", "Correo enviado a la siguiente pantalla: $correo")

        val intent = Intent(this, ActivityVerificarCodigo::class.java)
        intent.putExtra("correo", correo)
        startActivity(intent)
    }
}
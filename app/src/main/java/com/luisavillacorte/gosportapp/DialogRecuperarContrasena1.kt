package com.luisavillacorte.gosportapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.recuperarContrasena.RecuperarContrasenaApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.cambiarContrasena.CambiarContrasenaContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.RecuperarContrasenaContract
//import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.RecuperarContrasenaPresenter

import retrofit2.Retrofit

//class DialogRecuperarContrasena1 : AppCompatActivity(), RecuperarContrasenaContract.View {
//    private lateinit var presenter: RecuperarContrasenaContract.Presenter
//    private lateinit var correoInput: EditText
//    private lateinit var botonEnviarCodigo: Button
//    private lateinit var contenedor: LinearLayout
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_dialog_recuperar_contrasena1)
//
//        // Inicializar vistas
//        correoInput = findViewById(R.id.correoRecuperar)
//        botonEnviarCodigo = findViewById(R.id.btnRecuperar)
//        contenedor = findViewById(R.id.contenedor)
//
//        // Configurar el servicio usando RetrofitInstance
//        val apiService = RetrofitInstance.createService(RecuperarContrasenaApiService::class.java)
//        presenter = RecuperarContrasenaPresenter(this, apiService)
//
//        // Configurar el evento de clic
//        botonEnviarCodigo.setOnClickListener {
//            val correo = correoInput.text.toString()
//            if (correo.isNotEmpty()) {
//                presenter.solicitarCodigo(correo)
//            } else {
//                showError("Por favor ingrese un correo electrónico.")
//            }
//        }
//    }
//
//    override fun showLoading() {
//        // Mostrar un indicador de carga
//        // Puedes mostrar un ProgressBar aquí
//    }
//
//    override fun hideLoading() {
//        // Ocultar el indicador de carga
//        // Puedes ocultar el ProgressBar aquí
//    }
//
//    override fun showSuccess(message: String) {
//        // Mostrar mensaje de éxito
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }
//
//    override fun showError(message: String) {
//        // Mostrar mensaje de error
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }
//
//    override fun showEmailInput() {
//        // Implementar si es necesario para mostrar el campo de correo
//    }
//
//    override fun showVerificationInput() {
//        // Cambia la visibilidad del contenedor y agrega los campos para el código
//        // Aquí debes mostrar el layout donde se ingresa el código de verificación
//    }
//
//    override fun showNewPasswordInput() {
//        // Cambia la visibilidad del contenedor y agrega los campos para la nueva contraseña
//        // Aquí debes mostrar el layout donde se ingresa la nueva contraseña
//    }
//    override fun navigateToVerificationScreen(correo: String) {
//        val intent = Intent(this, dialog_codigo_verificacion1::class.java)
//        intent.putExtra("correo", correo)
//        startActivity(intent)
//    }
//}
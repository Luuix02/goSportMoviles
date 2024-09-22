package com.luisavillacorte.gosportapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.recuperarContrasena.RecuperarContrasenaApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.cambiarContrasena.CambiarContrasenaContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.cambiarContrasena.CambiarContrasenaPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.RecuperarContrasenaContract

//class recuperarContrasenaCheck : AppCompatActivity(), RecuperarContrasenaContract.View {

//    private lateinit var correoRecuperar: EditText
//    private lateinit var btnNewClave: Button
//    private lateinit var presenter: CambiarContrasenaContract.Presenter
//    private lateinit var correo: String
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_recuperar_contrasena_check)
//
//        // Inicializar vistas
//        correoRecuperar = findViewById(R.id.correoRecuperar)
//        btnNewClave = findViewById(R.id.btnNewClave)
//
//        // Inicializar el Presenter
////        val apiService = RetrofitInstance.createService(RecuperarContrasenaApiService::class.java)
////        presenter = CambiarContrasenaPresenter(this, apiService)
////
////        // Obtener el correo de la actividad anterior
////        correo = intent.getStringExtra("correo") ?: ""
////
////        // Configurar el evento de clic
////        btnNewClave.setOnClickListener {
////            val nuevaContrasena = correoRecuperar.text.toString()
////            if (nuevaContrasena.isNotEmpty()) {
////                presenter.cambiarContrasena(correo, nuevaContrasena)
////            } else {
////                showError("Por favor ingresa una nueva contraseña.")
////            }
////        }
//    }
//
//    // Implementación de los métodos del contrato de vista
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
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//        // Puedes redirigir a otra pantalla o cerrar la actividad
//        finish() // Cierra la actividad actual
//    }
//
//    override fun showError(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }
//
////    override fun showEmailInput() {
////        // Implementar la lógica para mostrar el campo de entrada de correo si es necesario
////        // Si no necesitas hacer nada, puedes dejar este método vacío
////    }
//    fun navigateToVerificationScreen(correo: String){
//
//    }
//}

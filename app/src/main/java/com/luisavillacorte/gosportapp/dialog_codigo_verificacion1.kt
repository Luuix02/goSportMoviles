package com.luisavillacorte.gosportapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.model.cambiarContrasena.CambiarContrasenaPresenter
import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.RecuperarContrasenaContract
//import com.luisavillacorte.gosportapp.jugador.adapters.model.recuperarContrasena.RecuperarContrasenaPresenter

//class dialog_codigo_verificacion1 : AppCompatActivity(), RecuperarContrasenaContract.View {
//    private lateinit var codigo1: EditText
//    private lateinit var codigo2: EditText
//    private lateinit var codigo3: EditText
//    private lateinit var codigo4: EditText
//    private lateinit var codigo5: EditText
//    private lateinit var codigo6: EditText
//    private lateinit var btnVerificarCodigo: Button
//    private lateinit var presenter: RecuperarContrasenaContract.Presenter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_dialog_codigo_verificacion1)
//
//        // Inicializar vistas
//        codigo1 = findViewById(R.id.codigo1)
//        codigo2 = findViewById(R.id.codigo2)
//        codigo3 = findViewById(R.id.codigo3)
//        codigo4 = findViewById(R.id.codigo4)
//        codigo5 = findViewById(R.id.codigo5)
//        codigo6 = findViewById(R.id.codigo6)
//        btnVerificarCodigo = findViewById(R.id.btnVerificarCodigo)
//
//        // Inicializar el Presenter
//        val apiService = RetrofitInstance.createService(dialog_codigo_verificacion1::class.java)
//        presenter = RecuperarContrasenaPresenter(this, apiService)
//
//        // Configurar el evento de clic
//        btnVerificarCodigo.setOnClickListener {
//            val codigo = getCodigoFromFields()
//            if (codigo.isNotEmpty()) {
//                presenter.verificarCodigo(codigo)
//            } else {
//                showError("Por favor ingresa el código de verificación.")
//            }
//        }
//    }
//
//    private fun getCodigoFromFields(): String {
//        return "${codigo1.text}${codigo2.text}${codigo3.text}${codigo4.text}${codigo5.text}${codigo6.text}"
//    }
//
//    override fun showLoading() {
//        // Mostrar un indicador de carga
//    }
//
//    override fun hideLoading() {
//        // Ocultar el indicador de carga
//    }
//
//    override fun showSuccess(message: String) {
//        // Redirigir a la actividad de cambiar contraseña
//        val intent = Intent(this, recuperarContrasenaCheck::class.java)
//        intent.putExtra("correo", intent.getStringExtra("correo")) // Asegúrate de pasar el correo
//        startActivity(intent)
//    }
//
//    override fun showError(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
//    }
//}
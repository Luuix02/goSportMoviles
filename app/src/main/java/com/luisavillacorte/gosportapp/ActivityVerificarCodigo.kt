package com.luisavillacorte.gosportapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
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

class ActivityVerificarCodigo : AppCompatActivity(), RecuperarContrasenaContract.VerificarCodigoView {

    private lateinit var presenter: RecuperarContrasenaContract.Presenter
    private lateinit var codigo1: EditText
    private lateinit var codigo2: EditText
    private lateinit var codigo3: EditText
    private lateinit var codigo4: EditText
    private lateinit var codigo5: EditText
    private lateinit var codigo6: EditText
    private lateinit var botonVerificar: Button
    private lateinit var correo: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verificar_codigo)

        codigo1 = findViewById(R.id.codigo1)
        codigo2 = findViewById(R.id.codigo2)
        codigo3 = findViewById(R.id.codigo3)
        codigo4 = findViewById(R.id.codigo4)
        codigo5 = findViewById(R.id.codigo5)
        codigo6 = findViewById(R.id.codigo6)

        botonVerificar = findViewById(R.id.btnVerificarCodigo)

        correo = intent.getStringExtra("correo") ?: ""
        Log.d("ActivityVerificarCodigo", "Correo recibido: $correo")

        val apiService = RetrofitInstance.createService(RecuperarContrasenaApiService::class.java)
        presenter = RecuperarContrasenaPresenter(apiService, verificarCodigoView = this)

        botonVerificar.setOnClickListener {
            val codigoVerificacion = getCodigoDeVerificacion()
            if (codigoVerificacion.length == 6) {
                Log.d("VerificarCodigo", "Código: $codigoVerificacion, Correo: $correo")
                showLoading()
                presenter.verificarCodigo(correo, codigoVerificacion)
            } else {
                showError("Por favor, ingresa un código de verificación válido.")
            }
        }


        setupTextWatchers()
    }

    private fun getCodigoDeVerificacion(): String {
        return "${codigo1.text}${codigo2.text}${codigo3.text}${codigo4.text}${codigo5.text}${codigo6.text}"
    }

    private fun setupTextWatchers() {
        val editTexts = listOf(codigo1, codigo2, codigo3, codigo4, codigo5, codigo6)

        for (i in editTexts.indices) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        editTexts[i + 1].requestFocus()
                    } else if (s?.isEmpty() == true && i > 0) {
                        editTexts[i - 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
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

    override fun navigateToChangePasswordScreen(correo: String) {
        val intent = Intent(this, ActivityCambiarContrasena::class.java)
        intent.putExtra("correo", correo)
        startActivity(intent)
    }
}
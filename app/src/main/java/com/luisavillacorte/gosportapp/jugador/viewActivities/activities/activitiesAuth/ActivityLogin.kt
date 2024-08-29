package com.luisavillacorte.gosportapp.jugador.viewActivities.activities.activitiesAuth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.luisavillacorte.gosportapp.R
import com.luisavillacorte.gosportapp.common.apiRetrofit.RetrofitInstance
import com.luisavillacorte.gosportapp.jugador.adapters.apiService.authService.ApiService
import com.luisavillacorte.gosportapp.jugador.adapters.model.login.LoginContract
import com.luisavillacorte.gosportapp.jugador.adapters.model.login.LoginPresenter
import com.luisavillacorte.gosportapp.jugador.viewActivities.activities.navegacioMenuApp.NavegacionMenuApp
import com.luisavillacorte.gosportapp.planillero.viewActivities.EquiposAsignadosPlanillero.PlanilleroFragment

class ActivityLogin : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginPresenter
    private var isContrasenaVisible: Boolean = false
    private lateinit var  passwordEditText: EditText
    private lateinit var  emailEditText: EditText
    private lateinit var loginApiService: ApiService


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.id_correo)

        passwordEditText = findViewById(R.id.id_contrasena)

        val textoCrearCuenta: TextView = findViewById(R.id.texto_crear_cuenta)

        val textoOlvidasteContrasena: TextView = findViewById(R.id.texto_olvidaste_contrasena)

        val loginButton = findViewById<Button>(R.id.btn_loginn)

        loginApiService = RetrofitInstance.createService(ApiService::class.java)

        passwordEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = passwordEditText.compoundDrawables[2]
                if (event.rawX >= (passwordEditText.right - drawableEnd.bounds.width())) {
                    toggleContrasenaVisibility()

                    return@setOnTouchListener true
                }
            }

            return@setOnTouchListener false
        }



        presenter = LoginPresenter(this, loginApiService, this)


        loginButton.setOnClickListener {
            val correo = emailEditText.text.toString()
            val contrasena = passwordEditText.text.toString()


            if (correo.isEmpty() || contrasena.isEmpty()) {
                showError("Por favor, completa todos los campos.")
            } else {
                Log.d("ActivityLogin", "Login with email: $correo")
                presenter.loginUser(correo, contrasena)
            }

        }



        textoCrearCuenta.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        textoOlvidasteContrasena.setOnClickListener {
            val intent = Intent(this, OlvidarContrasena::class.java)
            startActivity(intent)
        }
    }

    private fun toggleContrasenaVisibility() {
        if (isContrasenaVisible) {
            passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.ic_ver_contra, 0)
        } else {
            passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                R.drawable.ic_ver_contra, 0)
        }
        passwordEditText.setSelection(passwordEditText.text.length)
        isContrasenaVisible = !isContrasenaVisible
    }

    override fun showLoading(){
        //mostrar balon cargando
    }
    override fun hideLoading(){
        //ocultar balon cargando
    }
    override fun showError(message: String){
        Log.e("ActivityLogin", "Show error: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    override fun showSuccess(token: String) {
        Log.d("ActivityLogin", "Login successful,token: $token")
        Toast.makeText(this,"Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, NavegacionMenuApp::class.java)
        startActivity(intent)
        finish()
    }

    override fun navigateToPlanillero() {
        val intent = Intent(this, PlanilleroFragment::class.java)
        startActivity(intent)
        finish()
    }

    override fun navigateToJugador() {
        val intent = Intent(this, NavegacionMenuApp::class.java)
        startActivity(intent)
        finish()
    }
}
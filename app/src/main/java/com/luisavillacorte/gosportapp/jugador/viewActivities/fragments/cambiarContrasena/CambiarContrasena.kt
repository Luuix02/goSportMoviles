package com.luisavillacorte.gosportapp.jugador.viewActivities.fragments.cambiarContrasena

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.luisavillacorte.gosportapp.R

class CambiarContrasena : Fragment() {

    private var isCurrentPasswordVisible = false
    private var isNewPasswordVisible = false
    private var isConfirmNewPasswordVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cambiar_contrasena, container, false)

        val etCurrentPassword = view.findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPassword = view.findViewById<EditText>(R.id.etNewPassword)
        val etConfirmNewPassword = view.findViewById<EditText>(R.id.etConfirmNewPassword)

        etCurrentPassword.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = etCurrentPassword.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= (etCurrentPassword.right - drawableEnd.bounds.width())) {
                    isCurrentPasswordVisible = togglePasswordVisibility(etCurrentPassword, isCurrentPasswordVisible)
                    v.performClick() // Llamada para asegurar la accesibilidad
                    return@setOnTouchListener true
                }
            }
            false
        }

        etNewPassword.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = etNewPassword.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= (etNewPassword.right - drawableEnd.bounds.width())) {
                    isNewPasswordVisible = togglePasswordVisibility(etNewPassword, isNewPasswordVisible)
                    v.performClick() // Llamada para asegurar la accesibilidad
                    return@setOnTouchListener true
                }
            }
            false
        }

        etConfirmNewPassword.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = etConfirmNewPassword.compoundDrawables[2]
                if (drawableEnd != null && event.rawX >= (etConfirmNewPassword.right - drawableEnd.bounds.width())) {
                    isConfirmNewPasswordVisible = togglePasswordVisibility(etConfirmNewPassword, isConfirmNewPasswordVisible)
                    v.performClick() // Llamada para asegurar la accesibilidad
                    return@setOnTouchListener true
                }
            }
            false
        }

        return view
    }

    private fun togglePasswordVisibility(editText: EditText, isPasswordVisible: Boolean): Boolean {
        if (isPasswordVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ver_contra, 0)
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ver_contra, 0)
        }
        editText.setSelection(editText.text.length)
        return !isPasswordVisible
    }
}

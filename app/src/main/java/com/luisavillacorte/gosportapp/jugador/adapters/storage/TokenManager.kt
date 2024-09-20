package com.luisavillacorte.gosportapp.jugador.adapters.storage

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            apply()
        }
        Log.d("TokenManager", "Token guardado: $token")
    }

    fun getToken(): String? {
        val token = sharedPreferences.getString("auth_token", null)
        Log.d("TokenManager", "Token recuperado: $token")
        return token
    }

    fun saveUserEmail(email: String) {
        with(sharedPreferences.edit()) {
            putString("user_email", email)
            apply()
        }
        Log.d("TokenManager", "Email guardado: $email")
    }

    fun getUserEmail(): String? {
        val email = sharedPreferences.getString("user_email", null)
        Log.d("TokenManager", "Email recuperado: $email")
        return email
    }

    fun saveUserId(userId: String) {
        with(sharedPreferences.edit()) {
            putString("user_id", userId)
            apply()
        }
        Log.d("TokenManager", "User ID guardado: $userId")
    }

    fun getUserId(): String? {
        val userId = sharedPreferences.getString("user_id", null)
        Log.d("TokenManager", "User ID recuperado: $userId")
        return userId
    }
}

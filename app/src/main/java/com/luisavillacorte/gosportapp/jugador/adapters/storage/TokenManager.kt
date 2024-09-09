package com.luisavillacorte.gosportapp.jugador.adapters.storage

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("auth_token", token)
            apply()
        }
    }

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun saveUserEmail(email: String) {
        with(sharedPreferences.edit()) {
            putString("user_email", email)
            apply()
        }
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString("user_email", null)
    }

    fun saveUserId(userId: String) {
        with(sharedPreferences.edit()) {
            putString("user_id", userId)
            apply()
        }
    }

    fun getUserId(): String? {
        return sharedPreferences.getString("user_id", null)
    }
}

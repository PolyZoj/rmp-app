package com.ifmo.rmp.data.repository

import android.content.Context
import com.ifmo.rmp.data.api.ApiService
import com.ifmo.rmp.data.model.LoginRequest

class AuthRepository(
    private val apiService: ApiService,
    private val context: Context
) {
    suspend fun login(username: String, password: String): Result<Boolean> {
        return try {
            val request = LoginRequest(username = username, password = password)
            val response = apiService.login(request)
            
            // Save credentials to shared preferences
            val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString("user_id", response.id)
                putString("token", response.token)
                apply()
            }
            
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun isLoggedIn(): Boolean {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return !sharedPreferences.getString("token", "").isNullOrEmpty()
    }
    
    fun logout() {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
} 
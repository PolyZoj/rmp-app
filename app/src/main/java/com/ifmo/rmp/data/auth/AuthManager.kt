package com.ifmo.rmp.data.auth

import android.content.Context
import android.content.SharedPreferences
import com.ifmo.rmp.data.api.ApiService
import com.ifmo.rmp.data.api.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthManager private constructor(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    private val apiService: ApiService = NetworkModule.provideApiService(context)
    
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        val token = getToken()
        _isAuthenticated.value = token != null
    }

    private fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    private fun getUserId(): String? {
        return sharedPreferences.getString("user_id", null)
    }

    suspend fun validateToken(): Boolean {
        val token = getToken() ?: return false
        val userId = getUserId() ?: return false
        
        return try {
            // Пытаемся получить профиль пользователя с текущим токеном
            // Если токен недействителен, сервер вернет ошибку
            apiService.getUserProfile(userId, "Bearer $token")
            true
        } catch (e: Exception) {
            // Если произошла ошибка, считаем токен недействительным
            clearAuth()
            false
        }
    }

    private fun clearAuth() {
        sharedPreferences.edit().apply {
            remove("auth_token")
            remove("user_id")
            apply()
        }
        _isAuthenticated.value = false
    }

    companion object {
        @Volatile
        private var instance: AuthManager? = null

        fun getInstance(context: Context): AuthManager {
            return instance ?: synchronized(this) {
                instance ?: AuthManager(context.applicationContext).also { instance = it }
            }
        }
    }
} 
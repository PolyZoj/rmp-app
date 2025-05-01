package com.ifmo.rmp.data.repository

import android.content.Context
import com.ifmo.rmp.data.api.ApiService
import com.ifmo.rmp.data.api.NetworkModule
import com.ifmo.rmp.data.model.Profile

class UserRepository(private val apiService: ApiService) {
    
    companion object {
        @Volatile
        private var instance: UserRepository? = null
        
        fun getInstance(context: Context): UserRepository {
            return instance ?: synchronized(this) {
                instance ?: UserRepository(
                    NetworkModule.provideApiService(context)
                ).also { instance = it }
            }
        }
    }
    
    suspend fun getUserProfile(userId: String): Result<Profile> {
        return try {
            val profile = apiService.getUserProfile(userId)
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Example of how to get the current user ID from SharedPreferences
    fun getCurrentUserId(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_id", null)
    }
} 
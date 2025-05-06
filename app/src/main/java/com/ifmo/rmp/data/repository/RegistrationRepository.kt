package com.ifmo.rmp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.ifmo.rmp.data.api.ApiService
import com.ifmo.rmp.data.api.NetworkModule
import com.ifmo.rmp.data.model.RegistrationRequest
import com.ifmo.rmp.data.model.RegistrationResponse

class RegistrationRepository(private val apiService: ApiService, private val context: Context) {
    
    companion object {
        @Volatile
        private var instance: RegistrationRepository? = null
        
        fun getInstance(context: Context): RegistrationRepository {
            return instance ?: synchronized(this) {
                instance ?: RegistrationRepository(
                    NetworkModule.provideApiService(context),
                    context
                ).also { instance = it }
            }
        }
    }
    
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    }
    
    suspend fun register(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String,
        weight: Int,
        height: Int,
        birthDate: String,
        dailyStepGoal: Int,
        waterIntakeGoal: Int,
        calorieGoal: Int
    ): Result<RegistrationResponse> {
        return try {
            val request = RegistrationRequest(
                username = username,
                password = password,
                first_name = firstName,
                last_name = lastName,
                email = email,
                avatar_url = "1",
                weight = weight,
                height = height,
                birth_date = birthDate,
                unit_system = "METRIC",
                energy_system = "KCAL",
                health_goal = "AAA",
                daily_step_goal = dailyStepGoal,
                water_intake_goal = waterIntakeGoal,
                calorie_goal = calorieGoal,
                sleep_goal = 8.0f,
                workouts_goal = 4
            )
            val response = apiService.register(request)
            
            sharedPreferences.edit().apply {
                putString("auth_token", response.token)
                putString("user_id", response.id)
                apply()
            }
            
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 
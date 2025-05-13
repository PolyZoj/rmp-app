package com.ifmo.rmp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.ifmo.rmp.data.api.AuthApiService
import com.ifmo.rmp.data.api.NetworkModule
import com.ifmo.rmp.data.model.LoginRequest
import com.ifmo.rmp.data.model.LoginResponse
import com.ifmo.rmp.data.model.RegistrationRequest
import com.ifmo.rmp.data.model.RegistrationResponse
import androidx.core.content.edit

class AuthRepository private constructor(
    private val authApiService: AuthApiService,
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        @Volatile
        private var instance: AuthRepository? = null

        fun getInstance(context: Context): AuthRepository {
            return instance ?: synchronized(this) {
                instance ?: AuthRepository(
                    authApiService = NetworkModule.provideAuthApiService(context),
                    sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                ).also { instance = it }
            }
        }
    }

    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val request = LoginRequest(username = username, password = password)
            val response = authApiService.login(request)
            sharedPreferences.edit {
                putString("user_id", response.id)
                    .putString("token", response.token)
            }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
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
        calorieGoal: Int,
        workoutGoal: Int,
        avatarName: String
    ): Result<RegistrationResponse> {
        return try {
            val request = RegistrationRequest(
                username = username,
                password = password,
                first_name = firstName,
                last_name = lastName,
                email = email,
                avatar_url = avatarName,
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
                workouts_goal = workoutGoal
            )
            val response = authApiService.register(request)
            sharedPreferences.edit()
                .putString("user_id", response.id)
                .putString("token", response.token)
                .apply()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getString("token", null)?.isNotBlank() == true
    }

    fun logout() {
        sharedPreferences.edit().clear().apply()
    }
}
package com.ifmo.rmp.ui.screens.activities

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.repository.StatsRepository
import com.ifmo.rmp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ActivitiesViewModel(
    private val userRepository: UserRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

    private val _waterIntake = MutableStateFlow(0)
    val waterIntake: StateFlow<Int> = _waterIntake

    private val _workouts = MutableStateFlow(0)
    val workouts: StateFlow<Int> = _workouts

    private val _calories = MutableStateFlow(0)
    val calories: StateFlow<Int> = _calories

    private val _stepGoal = MutableStateFlow(10000)
    val stepGoal: StateFlow<Int> = _stepGoal

    private val _waterGoal = MutableStateFlow(10)
    val waterGoal: StateFlow<Int> = _waterGoal

    private val _workoutGoal = MutableStateFlow(2)
    val workoutGoal: StateFlow<Int> = _workoutGoal

    private val _calorieGoal = MutableStateFlow(5000) // Фиксированная цель, уточнить

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadUserData(context: Context, userId: String) {
        if (userId.isBlank()) {
            _errorMessage.value = "User ID is missing"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.getUserData(userId)
            result.onSuccess { userData ->
                _stepGoal.value = userData.daily_step_goal
                _waterGoal.value = userData.water_intake_goal
                _workoutGoal.value = userData.workouts_goal
                // _calorieGoal остается 5000, так как нет поля в UserDtoResponse
            }.onFailure { error ->
                _errorMessage.value = "Failed to load user data: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    fun loadDailyStats(context: Context, userId: String) {
        if (userId.isBlank()) {
            _errorMessage.value = "User ID is missing"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val currentDate = dateFormat.format(Calendar.getInstance().time)
            val result = statsRepository.getDailyStats(userId, currentDate)

            result.onSuccess { stats ->
                _steps.value = stats.calorie_count // Используется как шаги, как в ProfileViewModel
                _waterIntake.value = stats.water_count
                _workouts.value = stats.workouts_count
                _calories.value = stats.calorie_count
            }.onFailure { error ->
                _steps.value = 0
                _waterIntake.value = 0
                _workouts.value = 0
                _calories.value = 0
                _errorMessage.value = "Failed to load statistics: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
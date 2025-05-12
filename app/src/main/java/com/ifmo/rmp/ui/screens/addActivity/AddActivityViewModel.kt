package com.ifmo.rmp.ui.screens.addActivity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.AddStatsRequest
import com.ifmo.rmp.data.repository.StatsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AddActivityViewModel(
    private val statsRepository: StatsRepository,
    private val userId: String
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    fun addWater(context: Context, milliliters: Int) {
        if (userId.isBlank()) {
            _errorMessage.value = "User ID is missing"
            return
        }
        if (milliliters <= 0) {
            _errorMessage.value = "Water volume must be greater than 0 ml"
            return
        }

        viewModelScope.launch {
            val request = AddStatsRequest(id = userId, type = "water", add = milliliters)
            val result = statsRepository.addStats(request)
            result.onSuccess { response ->
                if (response.status) {
                    _successMessage.value = "Water added successfully"
                } else {
                    _errorMessage.value = "Failed to add water"
                }
            }.onFailure { error ->
                _errorMessage.value = "Error: ${error.message}"
            }
        }
    }

    fun addWorkout(context: Context, minutes: Int) {
        if (userId.isBlank()) {
            _errorMessage.value = "User ID is missing"
            return
        }
        if (minutes <= 0) {
            _errorMessage.value = "Workout time must be greater than 0 minutes"
            return
        }

        viewModelScope.launch {
            val request = AddStatsRequest(id = userId, type = "workout", add = minutes)
            val result = statsRepository.addStats(request)
            result.onSuccess { response ->
                if (response.status) {
                    _successMessage.value = "Workout added successfully"
                } else {
                    _errorMessage.value = "Failed to add workout"
                }
            }.onFailure { error ->
                _errorMessage.value = "Error: ${error.message}"
            }
        }
    }

    fun setErrorMessage(message: String?) {
        _errorMessage.value = message
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}
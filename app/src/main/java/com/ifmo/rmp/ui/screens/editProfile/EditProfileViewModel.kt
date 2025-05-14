package com.ifmo.rmp.ui.screens.editProfile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.UserUpdateRequest
import com.ifmo.rmp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _showSuccessMessage = mutableStateOf(false)
    val showSuccessMessage: State<Boolean> = _showSuccessMessage
    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

    fun loadUserData(userId: String) {
        viewModelScope.launch {
            val result = userRepository.getUserData(userId)
            result.onSuccess { user ->
                _uiState.update {
                    it.copy(
                        weight = user.weight.toString(),
                        dailyStepGoal = user.daily_step_goal.toString(),
                        waterIntakeGoal = user.water_intake_goal.toString(),
                        calorieGoal = user.calorie_goal.toString()
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(errorMessage = e.localizedMessage ?: "Failed to load user data")
                }
            }
        }
    }

    fun onWeightChange(value: String) {
        _uiState.update { it.copy(weight = value) }
    }

    fun onStepGoalChange(value: String) {
        _uiState.update { it.copy(dailyStepGoal = value) }
    }

    fun onWaterIntakeChange(value: String) {
        _uiState.update { it.copy(waterIntakeGoal = value) }
    }

    fun onCalorieGoalChange(value: String) {
        _uiState.update { it.copy(calorieGoal = value) }
    }

    fun saveProfile() {
        viewModelScope.launch {
            val request = UserUpdateRequest(
                weight = _uiState.value.weight.toFloatOrNull(),
                daily_step_goal = _uiState.value.dailyStepGoal.toIntOrNull(),
                water_intake_goal = _uiState.value.waterIntakeGoal.toIntOrNull(),
                calorie_goal = _uiState.value.calorieGoal.toIntOrNull()
            )

            val result = userRepository.updateUserProfile(request)
            result.onSuccess {
                _uiState.update { it.copy(isSaved = true, errorMessage = null) }
            }.onFailure { e ->
                _uiState.update { it.copy(errorMessage = e.localizedMessage ?: "Unknown error") }
            }
        }
        _showSuccessMessage.value = true
    }

    fun resetSuccessMessage() {
        _showSuccessMessage.value = false
    }
}

data class EditProfileUiState(
    val weight: String = "",
    val dailyStepGoal: String = "",
    val waterIntakeGoal: String = "",
    val calorieGoal: String = "",
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)


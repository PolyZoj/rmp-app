package com.ifmo.rmp.ui.screens.registration

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegistrationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState

    
    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(email = newEmail, errorMessage = "")
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword, errorMessage = "")
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = newConfirmPassword, errorMessage = "")
    }

    fun onFirstNameChange(newFirstName: String) {
        _uiState.value = _uiState.value.copy(firstName = newFirstName, errorMessage = "")
    }

    fun onLastNameChange(newLastName: String) {
        _uiState.value = _uiState.value.copy(lastName = newLastName, errorMessage = "")
    }

    fun onDateOfBirthChange(newDateOfBirth: String) {
        _uiState.value = _uiState.value.copy(dateOfBirth = newDateOfBirth, errorMessage = "")
    }

    fun onWeightChange(newWeight: String) {
        _uiState.value = _uiState.value.copy(weight = newWeight, errorMessage = "")
    }

    fun onHeightChange(newHeight: String) {
        _uiState.value = _uiState.value.copy(height = newHeight, errorMessage = "")
    }

    fun onStepGoalChange(newStepGoal: String) {
        _uiState.value = _uiState.value.copy(stepGoal = newStepGoal, errorMessage = "")
    }

    fun onWaterIntakeChange(newWaterIntake: String) {
        _uiState.value = _uiState.value.copy(waterIntake = newWaterIntake, errorMessage = "")
    }

    fun register() {
        val state = _uiState.value

        if (state.email.isBlank() || state.password.isBlank() || state.confirmPassword.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Fields cannot be empty")
            return
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(state.email)) {
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid email format")
            return
        }

        if (state.password.length < 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters")
            return
        }

        if (state.password != state.confirmPassword) {
            _uiState.value = _uiState.value.copy(errorMessage = "Passwords do not match")
            return
        }

        // TODO: Add backend request for registration
    }
}

// TODO: change data types for several variables
data class RegistrationUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: String = "",
    val weight: String = "",
    val height: String = "",
    val stepGoal: String = "",
    val waterIntake: String = "",
    val errorMessage: String = ""
)

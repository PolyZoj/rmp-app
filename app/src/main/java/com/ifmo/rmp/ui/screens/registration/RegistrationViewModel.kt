package com.ifmo.rmp.ui.screens.registration

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.repository.RegistrationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegistrationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState

    private var registrationRepository: RegistrationRepository? = null
    
    private fun getRegistrationRepository(context: Context): RegistrationRepository {
        if (registrationRepository == null) {
            registrationRepository = RegistrationRepository.getInstance(context)
        }
        return registrationRepository!!
    }

    fun onEmailChange(newEmail: String) {
        _uiState.value = _uiState.value.copy(
            email = newEmail,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(
            password = newPassword,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = newConfirmPassword,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(
            username = newUsername,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onFirstNameChange(newFirstName: String) {
        _uiState.value = _uiState.value.copy(
            firstName = newFirstName,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onLastNameChange(newLastName: String) {
        _uiState.value = _uiState.value.copy(
            lastName = newLastName,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onDateOfBirthChange(newDate: String) {
        val dateRegex = "^\\d{4}-\\d{2}-\\d{2}$".toRegex()
        if (!dateRegex.matches(newDate)) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Invalid date format. Use YYYY-MM-DD",
                isSuccess = false
            )
            return
        }
        
        _uiState.value = _uiState.value.copy(
            dateOfBirth = newDate,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onWeightChange(newWeight: String) {
        _uiState.value = _uiState.value.copy(
            weight = newWeight,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onHeightChange(newHeight: String) {
        _uiState.value = _uiState.value.copy(
            height = newHeight,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onStepGoalChange(newStepGoal: String) {
        _uiState.value = _uiState.value.copy(
            stepGoal = newStepGoal,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onWaterIntakeChange(newWaterIntake: String) {
        _uiState.value = _uiState.value.copy(
            waterIntake = newWaterIntake,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onCalorieGoalChange(newCalorieGoal: String) {
        _uiState.value = _uiState.value.copy(
            calorieGoal = newCalorieGoal,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onAvatarSelected(avatarId: Int) {
        _uiState.value = _uiState.value.copy(
            selectedAvatar = avatarId,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun register(context: Context) {
        val state = _uiState.value

        if (state.email.isBlank() || state.password.isBlank() || state.confirmPassword.isBlank() || 
            state.username.isBlank() || state.firstName.isBlank() || state.lastName.isBlank() ||
            state.dateOfBirth.isBlank() || state.weight.isBlank() || state.height.isBlank() ||
            state.stepGoal.isBlank() || state.waterIntake.isBlank() || state.calorieGoal.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "All fields are required")
            return
        }

        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(state.email)) {
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid email format")
            return
        }

        val dateRegex = "^\\d{4}-\\d{2}-\\d{2}$".toRegex()
        if (!dateRegex.matches(state.dateOfBirth)) {
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid date format. Use YYYY-MM-DD")
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

        try {
            val weight = state.weight.toInt()
            val height = state.height.toInt()
            val stepGoal = state.stepGoal.toInt()
            val waterIntake = state.waterIntake.toInt()
            val calorieGoal = state.calorieGoal.toInt()

            viewModelScope.launch {
                try {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                    
                    val repository = getRegistrationRepository(context)
                    val result = repository.register(
                        username = state.username,
                        password = state.password,
                        firstName = state.firstName,
                        lastName = state.lastName,
                        email = state.email,
                        weight = weight,
                        height = height,
                        birthDate = state.dateOfBirth,
                        dailyStepGoal = stepGoal,
                        waterIntakeGoal = waterIntake,
                        calorieGoal = calorieGoal,
                        avatarId = state.selectedAvatar
                    )
                    
                    result.fold(
                        onSuccess = { response ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isSuccess = true,
                                errorMessage = "",
                            )
                        },
                        onFailure = { exception ->
                            when (exception) {
                                is HttpException -> {
                                    if (exception.code() == 409) {
                                        _uiState.value = _uiState.value.copy(
                                            isLoading = false,
                                            errorMessage = "Email or username already exists"
                                        )
                                    } else {
                                        val errorBody = exception.response()?.errorBody()?.string()
                                        _uiState.value = _uiState.value.copy(
                                            isLoading = false,
                                            errorMessage = "Server error: ${exception.message()}\nError body: $errorBody"
                                        )
                                    }
                                }
                                else -> {
                                    _uiState.value = _uiState.value.copy(
                                        isLoading = false,
                                        errorMessage = "Network error: ${exception.message}"
                                    )
                                }
                            }
                        }
                    )
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Unexpected error: ${e.message}"
                    )
                }
            }
        } catch (e: NumberFormatException) {
            _uiState.value = _uiState.value.copy(errorMessage = "Please enter valid numbers for weight, height, and goals")
        }
    }

    data class RegistrationUiState(
        val email: String = "",
        val password: String = "",
        val confirmPassword: String = "",
        val username: String = "",
        val firstName: String = "",
        val lastName: String = "",
        val dateOfBirth: String = "",
        val weight: String = "",
        val height: String = "",
        val stepGoal: String = "",
        val waterIntake: String = "",
        val calorieGoal: String = "",
        val selectedAvatar: Int = 1,
        val errorMessage: String = "",
        val cursorPosition: Int = 0,
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false,
    )
}

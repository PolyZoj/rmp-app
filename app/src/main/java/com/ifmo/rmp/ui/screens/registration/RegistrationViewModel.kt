package com.ifmo.rmp.ui.screens.registration

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegistrationViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState

    private var authRepository: AuthRepository? = null

    private fun getAuthRepository(context: Context): AuthRepository {
        if (authRepository == null) {
            authRepository = AuthRepository.getInstance(context)
        }
        return authRepository!!
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

    fun onWorkoutsGoalChange(newWorkoutsGoal: String) {
        _uiState.value = _uiState.value.copy(
            workoutsGoal = newWorkoutsGoal,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun onAvatarSelected(avatarName: String) {
        _uiState.value = _uiState.value.copy(
            selectedAvatar = avatarName,
            errorMessage = "",
            isSuccess = false
        )
    }

    fun register(context: Context) {
        val state = _uiState.value

        // Проверка на заполненность всех полей
        if (state.email.isBlank() || state.password.isBlank() || state.confirmPassword.isBlank() ||
            state.username.isBlank() || state.firstName.isBlank() || state.lastName.isBlank() ||
            state.dateOfBirth.isBlank() || state.weight.isBlank() || state.height.isBlank() ||
            state.stepGoal.isBlank() || state.waterIntake.isBlank() || state.calorieGoal.isBlank() ||
            state.workoutsGoal.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "All fields are required")
            return
        }

        // Проверка формата email
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(state.email)) {
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid email format")
            return
        }

        // Проверка формата даты
        val dateRegex = "^\\d{4}-\\d{2}-\\d{2}$".toRegex()
        if (!dateRegex.matches(state.dateOfBirth)) {
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid date format. Use YYYY-MM-DD")
            return
        }

        // Проверка длины пароля
        if (state.password.length < 6) {
            _uiState.value = _uiState.value.copy(errorMessage = "Password must be at least 6 characters")
            return
        }

        // Проверка совпадения паролей
        if (state.password != state.confirmPassword) {
            _uiState.value = _uiState.value.copy(errorMessage = "Passwords do not match")
            return
        }

        // Валидация числовых полей
        try {
            val weight = state.weight.toInt()
            val height = state.height.toInt()
            val stepGoal = state.stepGoal.toInt()
            val waterIntake = state.waterIntake.toInt()
            val calorieGoal = state.calorieGoal.toInt()
            val workoutsGoal = state.workoutsGoal.toInt()

            // Проверка на неотрицательность
            if (weight < 0 || height < 0 || stepGoal < 0 || waterIntake < 0 || calorieGoal < 0 || workoutsGoal < 0) {
                _uiState.value = _uiState.value.copy(errorMessage = "Numeric fields cannot be negative")
                return
            }

            // Проверка минимального роста
            if (height < 130) {
                _uiState.value = _uiState.value.copy(errorMessage = "Height must be at least 130 cm")
                return
            }

            viewModelScope.launch {
                try {
                    _uiState.value = _uiState.value.copy(isLoading = true)

                    val repository = getAuthRepository(context)
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
                        workoutGoal = workoutsGoal,
                        avatarName = state.selectedAvatar
                    )

                    result.fold(
                        onSuccess = { response ->
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isSuccess = true,
                                errorMessage = ""
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
        val workoutsGoal: String = "",
        val selectedAvatar: String = "e_avatar_1",
        val errorMessage: String = "",
        val cursorPosition: Int = 0,
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false
    )
}
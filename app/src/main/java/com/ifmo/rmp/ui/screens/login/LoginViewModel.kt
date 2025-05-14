package com.ifmo.rmp.ui.screens.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private var authRepository: AuthRepository? = null

    private fun getAuthRepository(context: Context): AuthRepository {
        if (authRepository == null) {
            authRepository = AuthRepository.getInstance(context)
        }
        return authRepository!!
    }

    fun onUsernameChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(
            username = newUsername,
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

    fun login(context: Context) {
        val username = _uiState.value.username
        val password = _uiState.value.password

        if (username.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Fields cannot be empty")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val repository = getAuthRepository(context)
                val result = repository.login(username, password)

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
                                if (exception.code() == 401) {
                                    _uiState.value = _uiState.value.copy(
                                        isLoading = false,
                                        errorMessage = "Invalid username or password"
                                    )
                                } else {
                                    _uiState.value = _uiState.value.copy(
                                        isLoading = false,
                                        errorMessage = "Server error: ${exception.message}"
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
    }

    data class LoginUiState(
        val username: String = "",
        val password: String = "",
        val errorMessage: String = "",
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false
    )
}
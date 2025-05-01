package com.ifmo.rmp.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import android.content.Context
import retrofit2.HttpException

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8081/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val authApi: AuthApi = retrofit.create(AuthApi::class.java)

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

                val request = LoginRequest(username = username, password = password)
                val response = authApi.login(request)

                val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("user_id", response.id)
                    putString("token", response.token)
                    apply()
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    errorMessage = ""
                )
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Invalid username or password"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Server error: ${e.message}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error: ${e.message}"
                )
            }
        }
    }

    interface AuthApi {
        @POST("api/v1/auth/login")
        suspend fun login(@Body request: LoginRequest): LoginResponse
    }

    data class LoginRequest(
        val username: String,
        val password: String
    )

    data class LoginResponse(
        val id: String,
        val token: String
    )

    data class LoginUiState(
        val username: String = "",
        val password: String = "",
        val errorMessage: String = "",
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false
    )
}
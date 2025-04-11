import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

    fun onEmailChange(newEmail: String) {
        val error = if (newEmail.isNotEmpty() && !_emailRegex.matches(newEmail)) {
            "Invalid email format"
        } else ""

        _uiState.value = _uiState.value.copy(
            email = newEmail,
            errorMessage = error,
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

    fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(errorMessage = "Fields cannot be empty")
            return
        }

        if (!_emailRegex.matches(email)) {
            _uiState.value = _uiState.value.copy(errorMessage = "Invalid email format")
            return
        }

        if (password.length < 6) {
            _uiState.value =
                _uiState.value.copy(errorMessage = "Password must be at least 6 characters")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // TODO: добавить api, когда напишут бэк
                delay(1500)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Login failed: ${e.message ?: "Unknown error"}"
                )
            }
        }
    }
}

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)

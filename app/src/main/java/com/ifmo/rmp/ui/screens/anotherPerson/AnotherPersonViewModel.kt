package com.ifmo.rmp.ui.screens.anotherPerson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.UserDtoResponse
import com.ifmo.rmp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnotherPersonViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableStateFlow<UserDtoResponse?>(null)
    val user: StateFlow<UserDtoResponse?> = _user

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadUser(userId: String) {
        viewModelScope.launch {
            val result = userRepository.getUserData(userId)
            result
                .onSuccess { userData -> _user.value = userData }
                .onFailure { error -> _errorMessage.value = "Ошибка загрузки пользователя: ${error.message}" }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}



package com.ifmo.rmp.ui.screens.anotherPerson

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.UserDtoResponse
import com.ifmo.rmp.data.repository.UserRepository
import com.ifmo.rmp.ui.components.FriendButtonState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnotherPersonViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableStateFlow<UserDtoResponse?>(null)
    val user: StateFlow<UserDtoResponse?> = _user

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _friendButtonState = MutableStateFlow(FriendButtonState.AddFriend)
    val friendButtonState: StateFlow<FriendButtonState> = _friendButtonState

    fun loadUser(userId: String) {
        viewModelScope.launch {
            val result = userRepository.getUserData(userId)
            result
                .onSuccess { userData ->
                    _user.value = userData
                    _friendButtonState.value = when (userData.status) {
                        "YourFriend" -> FriendButtonState.RemoveFriend
                        "InviteSent" -> FriendButtonState.InviteSent
                        "NotYourFriend" -> FriendButtonState.AddFriend
                        else -> FriendButtonState.AddFriend
                    }
                }
                .onFailure { error ->
                    _errorMessage.value = "Ошибка загрузки пользователя: ${error.message}"
                }
        }
    }

    fun addFriend(friendId: Int) {
        viewModelScope.launch {
            val result = userRepository.addFriend(friendId)
            result
                .onSuccess { response ->
                    if (response.success == "true") {
                        _friendButtonState.value = FriendButtonState.InviteSent
                    } else {
                        _errorMessage.value = "Не удалось отправить заявку"
                    }
                }
                .onFailure { error ->
                    _errorMessage.value = "Ошибка при добавлении в друзья: ${error.message}"
                }
        }
    }

    fun removeFriend(friendId: Int) {
        viewModelScope.launch {
            val result = userRepository.removeFriend(friendId)
            result
                .onSuccess { response ->
                    if (response.success == "true") {
                        _friendButtonState.value = FriendButtonState.AddFriend
                    } else {
                        _errorMessage.value = "Не удалось удалить из друзей"
                    }
                }
                .onFailure { error ->
                    _errorMessage.value = "Ошибка при удалении из друзей: ${error.message}"
                }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}




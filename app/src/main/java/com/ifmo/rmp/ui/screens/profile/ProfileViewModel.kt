package com.ifmo.rmp.ui.screens.profile

import androidx.lifecycle.ViewModel
import android.content.Context
import android.provider.Settings.Global.putString
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.FriendStructure
import com.ifmo.rmp.data.model.UserDtoResponse
import com.ifmo.rmp.data.repository.ClubRepository
import com.ifmo.rmp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableStateFlow<UserDtoResponse?>(null)
    val user: StateFlow<UserDtoResponse?> = _user

    private val _friends = MutableStateFlow<List<FriendStructure>>(emptyList())
    val friends: StateFlow<List<FriendStructure>> = _friends

    private val _searchResults = MutableStateFlow<List<FriendStructure>>(emptyList())
    val searchResults: StateFlow<List<FriendStructure>> = _searchResults

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun loadUser(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.getUserData(userId)
            result.onSuccess {
                Log.d("ProfileViewModel", "Пользователь загружен: $it")
                _user.value = it
            }.onFailure {
//
//                _errorMessage.value = "Ошибка загрузки пользователя: ${it.message}"
            }
            _isLoading.value = false
        }
    }

    fun loadFriends() {
        viewModelScope.launch {
            val result = userRepository.getFriendsList()
            result.onSuccess {
                _friends.value = it.friends
            }.onFailure {
                _errorMessage.value = "Ошибка загрузки друзей: ${it.message}"
            }
        }
    }

    fun searchFriends(query: String) {
        viewModelScope.launch {
            val result = userRepository.findFriends(query)
            result.onSuccess {
                _searchResults.value = it.possible_friends
            }.onFailure {
                _errorMessage.value = "Ошибка поиска друзей: ${it.message}"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}


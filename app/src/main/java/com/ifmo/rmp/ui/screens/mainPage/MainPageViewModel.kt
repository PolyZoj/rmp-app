package com.ifmo.rmp.ui.screens.mainPage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.FriendStructure
import com.ifmo.rmp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _isNotificationsVisible = MutableStateFlow(false)
    val isNotificationsVisible: StateFlow<Boolean> = _isNotificationsVisible

    private val _friendRequests = MutableStateFlow<List<FriendStructure>>(emptyList())
    val friendRequests: StateFlow<List<FriendStructure>> = _friendRequests

    private val _userFullName = MutableStateFlow("User")
    val userFullName: StateFlow<String> = _userFullName

    fun loadUserData(userId: String) {
        if (userId.isBlank()) return

        viewModelScope.launch {
            val result = userRepository.getUserData(userId)
            result.onSuccess {
                _userFullName.value = "${it.first_name} ${it.last_name}"
            }.onFailure {
                _userFullName.value = "User"
            }
        }
    }

    fun loadNotifications() {
        viewModelScope.launch {
            val result = userRepository.getNotificationList()
            result.onSuccess {
                _friendRequests.value = it.friend_requests
            }.onFailure {
                _friendRequests.value = emptyList()
            }
        }
    }

    fun showNotificationsDialog() {
        _isNotificationsVisible.value = true
        loadNotifications()
    }

    fun hideNotificationsDialog() {
        _isNotificationsVisible.value = false
    }

    fun acceptRequest(userId: Int) {
        viewModelScope.launch {
            userRepository.acceptFriendRequest(userId)
                .onSuccess {
                    _friendRequests.value = _friendRequests.value.filterNot { it.user_id == userId.toString() }
                }
        }
    }

    fun declineRequest(userId: Int) {
        viewModelScope.launch {
            userRepository.denyFriendRequest(userId)
                .onSuccess {
                    _friendRequests.value = _friendRequests.value.filterNot { it.user_id == userId.toString() }
                }
        }
    }
}



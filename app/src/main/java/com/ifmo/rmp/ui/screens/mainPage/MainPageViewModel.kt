package com.ifmo.rmp.ui.screens.mainPage

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.Achievement
import com.ifmo.rmp.data.model.FriendStructure
import com.ifmo.rmp.data.repository.ChallengesRepository
import com.ifmo.rmp.data.repository.StatsRepository
import com.ifmo.rmp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainPageViewModel(
    private val userRepository: UserRepository,
    private val challengesRepository: ChallengesRepository
) : ViewModel() {

    private val _isNotificationsVisible = MutableStateFlow(false)
    val isNotificationsVisible: StateFlow<Boolean> = _isNotificationsVisible

    private val _friendRequests = MutableStateFlow<List<FriendStructure>>(emptyList())
    val friendRequests: StateFlow<List<FriendStructure>> = _friendRequests

    private val _userFullName = MutableStateFlow("User")
    val userFullName: StateFlow<String> = _userFullName

    private val _steps = MutableStateFlow(0)
    val steps: StateFlow<Int> = _steps

    private val _waterIntake = MutableStateFlow(0)
    val waterIntake: StateFlow<Int> = _waterIntake

    private val _workouts = MutableStateFlow(0)
    val workouts: StateFlow<Int> = _workouts

    private val _stepGoal = MutableStateFlow(10000)
    val stepGoal: StateFlow<Int> = _stepGoal

    private val _waterGoal = MutableStateFlow(10)
    val waterGoal: StateFlow<Int> = _waterGoal

    private val _workoutGoal = MutableStateFlow(2)
    val workoutGoal: StateFlow<Int> = _workoutGoal

    private val _stepPercentage = MutableStateFlow(0)
    val stepPercentage: StateFlow<Int> = _stepPercentage

    private val _waterPercentage = MutableStateFlow(0)
    val waterPercentage: StateFlow<Int> = _waterPercentage

    private val _workoutPercentage = MutableStateFlow(0)
    val workoutPercentage: StateFlow<Int> = _workoutPercentage

    private val _challengesList = MutableStateFlow<List<Achievement>>(emptyList())
    val challengesList: StateFlow<List<Achievement>> = _challengesList

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadUserData(userId: String) {
        if (userId.isBlank()) {
            _errorMessage.value = "User ID is missing"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val userResult = userRepository.getUserData(userId)
            val achievementsResult = challengesRepository.getAchievementsById(userId)
            userResult.onSuccess { userData ->
                _userFullName.value = "${userData.first_name} ${userData.last_name}"
                _stepGoal.value = userData.daily_step_goal
                _waterGoal.value = userData.water_intake_goal
                _workoutGoal.value = userData.workouts_goal
                updatePercentages()
            }.onFailure { error ->
                _userFullName.value = "User"
                _errorMessage.value = "Failed to load user data: ${error.message}"
            }

            achievementsResult.onSuccess { achievements ->
                _challengesList.value = achievements.achievements
            }.onFailure { error ->
                _errorMessage.value = "Failed to load achievements: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.getNotificationList()
            result.onSuccess { notifications ->
                _friendRequests.value = notifications.friend_requests
            }.onFailure { error ->
                _friendRequests.value = emptyList()
                _errorMessage.value = "Failed to load notifications: ${error.message}"
            }
            _isLoading.value = false
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
            _isLoading.value = true
            userRepository.acceptFriendRequest(userId)
                .onSuccess {
                    _friendRequests.value = _friendRequests.value.filterNot { it.user_id == userId.toString() }
                }.onFailure { error ->
                    _errorMessage.value = "Failed to accept friend request: ${error.message}"
                }
            _isLoading.value = false
        }
    }

    fun declineRequest(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            userRepository.denyFriendRequest(userId)
                .onSuccess {
                    _friendRequests.value = _friendRequests.value.filterNot { it.user_id == userId.toString() }
                }.onFailure { error ->
                    _errorMessage.value = "Failed to decline friend request: ${error.message}"
                }
            _isLoading.value = false
        }
    }

    fun loadStats(context: Context, userId: String) {
        if (userId.isBlank()) {
            _errorMessage.value = "User ID is missing"
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val statsRepository = StatsRepository.getInstance(context)
            val result = statsRepository.getStats(userId)

            result.onSuccess { stats ->
                _steps.value = stats.steps_count
                _waterIntake.value = stats.water_count
                _workouts.value = stats.workouts_count
                updatePercentages()
            }.onFailure { error ->
                _steps.value = 0
                _waterIntake.value = 0
                _workouts.value = 0
                _stepPercentage.value = 0
                _waterPercentage.value = 0
                _workoutPercentage.value = 0
                _errorMessage.value = "Failed to load statistics: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun updatePercentages() {
        _stepPercentage.value = if (_stepGoal.value > 0) ((_steps.value.toFloat() / _stepGoal.value) * 100).toInt() else 0
        _waterPercentage.value = if (_waterGoal.value > 0) ((_waterIntake.value.toFloat() / _waterGoal.value) * 100).toInt() else 0
        _workoutPercentage.value = if (_workoutGoal.value > 0) ((_workouts.value.toFloat() / _workoutGoal.value) * 100).toInt() else 0
    }
}
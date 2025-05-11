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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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

    fun loadUserData(userId: String) {
        if (userId.isBlank()) return

        viewModelScope.launch {
            val result = userRepository.getUserData(userId)
            val achievements = challengesRepository.getAchievementsById(userId)
            result.onSuccess {
                _userFullName.value = "${it.first_name} ${it.last_name}"
                _stepGoal.value = it.daily_step_goal
                _waterGoal.value = it.water_intake_goal
                _workoutGoal.value = it.workouts_goal
                _stepPercentage.value = if (_stepGoal.value > 0) ((_steps.value.toFloat() / _stepGoal.value) * 100).toInt() else 0
                _waterPercentage.value = if (_waterGoal.value > 0) ((_waterIntake.value.toFloat() / _waterGoal.value) * 100).toInt() else 0
                _workoutPercentage.value = if (_workoutGoal.value > 0) ((_workouts.value.toFloat() / _workoutGoal.value) * 100).toInt() else 0
            }.onFailure {
                _userFullName.value = "User"
            }

            achievements.onSuccess {
                _challengesList.value = it
            }
        }
    }

    private fun loadNotifications() {
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

    fun loadDailyStats(context: Context, userId: String) {
        if (userId.isBlank()) return

        viewModelScope.launch {
            val statsRepository = StatsRepository.getInstance(context)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val currentDate = dateFormat.format(Calendar.getInstance().time)
            val result = statsRepository.getDailyStats(userId, currentDate)

            result.onSuccess { stats ->
                _steps.value = stats.calorie_count
                _waterIntake.value = stats.water_count
                _workouts.value = stats.workouts_count

                _stepPercentage.value = if (_stepGoal.value > 0) ((_steps.value.toFloat() / _stepGoal.value) * 100).toInt() else 0
                _waterPercentage.value = if (_waterGoal.value > 0) ((_waterIntake.value.toFloat() / _waterGoal.value) * 100).toInt() else 0
                _workoutPercentage.value = if (_workoutGoal.value > 0) ((_workouts.value.toFloat() / _workoutGoal.value) * 100).toInt() else 0
            }.onFailure {
                _steps.value = 0
                _waterIntake.value = 0
                _workouts.value = 0
                _stepPercentage.value = 0
                _waterPercentage.value = 0
                _workoutPercentage.value = 0
            }
        }
    }
}
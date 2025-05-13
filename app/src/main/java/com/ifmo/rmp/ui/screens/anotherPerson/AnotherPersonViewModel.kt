package com.ifmo.rmp.ui.screens.anotherPerson

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.UserDtoResponse
import com.ifmo.rmp.data.repository.ClubRepository
import com.ifmo.rmp.data.repository.StatsRepository
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

    private val _level = MutableStateFlow(0)
    val level: StateFlow<Int> = _level

    private val _xp = MutableStateFlow(0)
    val xp: StateFlow<Int> = _xp

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _clubName = MutableStateFlow("No club")
    val clubName: StateFlow<String> = _clubName

    fun loadUser(userId: String) {
    viewModelScope.launch {
        try {
            if (userId.isBlank()) {
                _errorMessage.value = "Invalid user ID"
                _isLoading.value = false
                return@launch
            }

            _isLoading.value = true
            val result = userRepository.getUserData(userId)
            result.onSuccess { userData ->
                _user.value = userData
                if (userData.status != null) {
                    _friendButtonState.value = when (userData.status) {
                        "YourFriend" -> FriendButtonState.RemoveFriend
                        "InviteSent" -> FriendButtonState.InviteSent
                        "NotYourFriend" -> FriendButtonState.AddFriend
                        else -> FriendButtonState.AddFriend
                    }
                } else {
                    _friendButtonState.value = FriendButtonState.AddFriend
                }
                _stepGoal.value = userData.daily_step_goal
                _waterGoal.value = userData.water_intake_goal
                _workoutGoal.value = userData.workouts_goal
                _stepPercentage.value = if (_stepGoal.value > 0) ((_steps.value.toFloat() / _stepGoal.value) * 100).toInt() else 0
                _waterPercentage.value = if (_waterGoal.value > 0) ((_waterIntake.value.toFloat() / _waterGoal.value) * 100).toInt() else 0
                _workoutPercentage.value = if (_workoutGoal.value > 0) ((_workouts.value.toFloat() / _workoutGoal.value) * 100).toInt() else 0
            }.onFailure { error ->
                _errorMessage.value = "Failed to load user: ${error.message}"
            }
            _isLoading.value = false
        } catch (e: Exception) {
            _errorMessage.value = "Error occurred: ${e.message ?: "Unknown error"}"
            _isLoading.value = false
        }
    }
}

    fun addFriend(friendId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.addFriend(friendId)
            result.onSuccess { response ->
                if (response.success == "true") {
                    _friendButtonState.value = FriendButtonState.InviteSent
                } else {
                    _errorMessage.value = "Failed to send friend request"
                }
            }.onFailure { error ->
                _errorMessage.value = "Failed to add friend: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    fun removeFriend(friendId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = userRepository.removeFriend(friendId)
            result.onSuccess { response ->
                if (response.success == "true") {
                    _friendButtonState.value = FriendButtonState.AddFriend
                } else {
                    _errorMessage.value = "Failed to remove friend"
                }
            }.onFailure { error ->
                _errorMessage.value = "Failed to remove friend: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
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
                _level.value = stats.level
                _xp.value = stats.xp
                updatePercentages()
            }.onFailure { error ->
                _steps.value = 0
                _waterIntake.value = 0
                _workouts.value = 0
                _level.value = 0
                _xp.value = 0
                _stepPercentage.value = 0
                _waterPercentage.value = 0
                _workoutPercentage.value = 0
                _errorMessage.value = "Failed to load statistics: ${error.message}"
            }
            _isLoading.value = false
        }
    }

    private fun updatePercentages() {
        _stepPercentage.value = if (_stepGoal.value > 0) ((_steps.value.toFloat() / _stepGoal.value) * 100).toInt() else 0
        _waterPercentage.value = if (_waterGoal.value > 0) ((_waterIntake.value.toFloat() / _waterGoal.value) * 100).toInt() else 0
        _workoutPercentage.value = if (_workoutGoal.value > 0) ((_workouts.value.toFloat() / _workoutGoal.value) * 100).toInt() else 0
    }
    
    fun loadClubName(context: Context, clubId: Int?) {
        if (clubId == null || clubId == 0) {
            _clubName.value = "No club"
            return
        }

        viewModelScope.launch {
            try {
                val clubRepository = ClubRepository.getInstance(context)
                val result = clubRepository.getClubInfo(clubId.toString())

                result.fold(
                    onSuccess = { club ->
                        _clubName.value = club.name ?: "Unnamed Club"
                    },
                    onFailure = {
                        _clubName.value = "Club #$clubId"
                        _errorMessage.value = "Failed to load club information: ${it.message}"
                    }
                )
            } catch (e: Exception) {
                _clubName.value = "Club #$clubId"
                _errorMessage.value = "Failed to load club information: ${e.message}"
            }
        }
    }
}
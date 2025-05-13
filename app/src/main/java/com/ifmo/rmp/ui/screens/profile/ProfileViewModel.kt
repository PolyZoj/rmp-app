package com.ifmo.rmp.ui.screens.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.Achievement
import com.ifmo.rmp.data.model.FriendStructure
import com.ifmo.rmp.data.model.UserDtoResponse
import com.ifmo.rmp.data.repository.ChallengesRepository
import com.ifmo.rmp.data.repository.ClubRepository
import com.ifmo.rmp.data.repository.StatsRepository
import com.ifmo.rmp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val challengesRepository: ChallengesRepository
) : ViewModel() {

    private val _user = MutableStateFlow<UserDtoResponse?>(null)
    val user: StateFlow<UserDtoResponse?> = _user

    private val _friends = MutableStateFlow<List<FriendStructure>>(emptyList())
    val friends: StateFlow<List<FriendStructure>> = _friends

    private val _searchResults = MutableStateFlow<List<FriendStructure>>(emptyList())
    val searchResults: StateFlow<List<FriendStructure>> = _searchResults

    private val _isStatsLoading = MutableStateFlow(false)
    val isStatsLoading: StateFlow<Boolean> = _isStatsLoading

    private val _isSearchLoading = MutableStateFlow(false)
    val isSearchLoading: StateFlow<Boolean> = _isSearchLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

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

    private val _clubName = MutableStateFlow("No club")
    val clubName: StateFlow<String> = _clubName

    private val _challengesList = MutableStateFlow<List<Achievement>>(emptyList())
    val challengesList: StateFlow<List<Achievement>> = _challengesList

    fun loadUser(userId: String) {
        viewModelScope.launch {
            _isStatsLoading.value = true
            val result = userRepository.getUserData(userId)
            val achievements = challengesRepository.getAchievementsById(userId)

            result.onSuccess {
                _user.value = it
                _stepGoal.value = it.daily_step_goal
                _waterGoal.value = it.water_intake_goal
                _workoutGoal.value = it.workouts_goal
                updatePercentages()
            }.onFailure {
                _errorMessage.value = "Failed to load user: ${it.message}"
            }

            achievements.onSuccess {
                _challengesList.value = it.achievements
            }.onFailure {
                _errorMessage.value = "Failed to load achievements: ${it.message}"
            }

            _isStatsLoading.value = false
        }
    }

    fun loadFriends() {
        viewModelScope.launch {
            val result = userRepository.getFriendsList()
            result.onSuccess {
                _friends.value = it.friends
            }.onFailure {
                _errorMessage.value = "Failed to load friends: ${it.message}"
            }
        }
    }

    fun searchFriends(query: String) {
        viewModelScope.launch {
            _isSearchLoading.value = true
            val result = userRepository.findFriends(query)
            result.onSuccess {
                _searchResults.value = it.possible_friend
                Log.d("ProfileViewModel", "Search results updated: ${it.possible_friend}")
            }.onFailure {
                _errorMessage.value = "Failed to search friends: ${it.message}"
            }
            _isSearchLoading.value = false
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun loadStats(context: Context, userId: String) {
        if (userId.isBlank()) {
            _errorMessage.value = "User ID is missing"
            return
        }

        viewModelScope.launch {
            Log.d("ProfileViewModel", "loadStats called for userId: $userId")
            _isStatsLoading.value = true
            val statsRepository = StatsRepository.getInstance(context)
            val result = statsRepository.getStats(userId)

            result.onSuccess { stats ->
                _steps.value = stats.steps_count
                _waterIntake.value = stats.water_count
                _workouts.value = stats.workouts_count
                _level.value = stats.level
                _xp.value = stats.xp
                updatePercentages()
            }.onFailure {
                _steps.value = 0
                _waterIntake.value = 0
                _workouts.value = 0
                _level.value = 0
                _xp.value = 0
                _stepPercentage.value = 0
                _waterPercentage.value = 0
                _workoutPercentage.value = 0
                _errorMessage.value = "Failed to load statistics: ${it.message}"
            }
            _isStatsLoading.value = false
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
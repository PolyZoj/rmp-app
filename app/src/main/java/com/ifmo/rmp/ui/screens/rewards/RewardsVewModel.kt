package com.ifmo.rmp.ui.screens.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.Achievement
import com.ifmo.rmp.data.repository.ChallengesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RewardsVewModel(private val challengesRepository: ChallengesRepository) : ViewModel() {

    private val _challengesList = MutableStateFlow<List<Achievement>>(emptyList())
    val challengesList: StateFlow<List<Achievement>> = _challengesList

    fun loadChallenges(userId: String) {
        viewModelScope.launch {

            val achievements = challengesRepository.getAchievementsById(userId)

            achievements.onSuccess {
                _challengesList.value = it
            }

        }
    }

}
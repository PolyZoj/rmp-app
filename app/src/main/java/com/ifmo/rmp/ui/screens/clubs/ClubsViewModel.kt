package com.ifmo.rmp.ui.screens.clubs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.ClubCreateResponse
import com.ifmo.rmp.data.model.ClubInfoResponse
import com.ifmo.rmp.data.model.ClubMemberResponse
import com.ifmo.rmp.data.repository.ClubRepository
import com.ifmo.rmp.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ClubsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ClubsUiState())
    val uiState: StateFlow<ClubsUiState> = _uiState

    private var clubRepository: ClubRepository? = null

    private fun getClubRepository(context: Context): ClubRepository {
        if (clubRepository == null) {
            clubRepository = ClubRepository.getInstance(context)
        }
        return clubRepository!!
    }

    fun onClubNameChange(newName: String) {
        _uiState.value = _uiState.value.copy(
            clubName = newName,
            errorMessage = ""
        )
    }

    fun onClubDescriptionChange(newDescription: String) {
        _uiState.value = _uiState.value.copy(
            clubDescription = newDescription,
            errorMessage = ""
        )
    }

    fun getClubInfo(context: Context, clubId: String? = null) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Get the current user ID from SharedPreferences
                val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val currentUserId = sharedPreferences.getString("user_id", "") ?: ""
                
                // If clubId is not provided, get it from the user's profile
                val finalClubId = if (clubId.isNullOrBlank() && currentUserId.isNotBlank()) {
                    val userRepository = UserRepository.getInstance(context)
                    val userResult = userRepository.getUserData(currentUserId)
                    
                    userResult.fold(
                        onSuccess = { userData ->
                            // Convert club_id from Int to String
                            userData.club_id?.toString() ?: ""
                        },
                        onFailure = { "" }
                    )
                } else {
                    clubId ?: ""
                }
                
                println("Getting club info for clubId: $finalClubId")
                
                // Only proceed if we have a valid club ID
                if (finalClubId.isNotBlank()) {
                    val repository = getClubRepository(context)
                    val result = repository.getClubInfo(finalClubId)

                    result.fold(
                        onSuccess = { clubInfo ->
                            // Mark the club as joined if the user is a member
                            val isJoined = clubInfo.members.contains(currentUserId)
                            val updatedClubInfo = clubInfo.copy(isJoined = isJoined)
                            
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                clubInfo = updatedClubInfo,
                                errorMessage = ""
                            )
                            println("Club info loaded successfully: ${updatedClubInfo.name}, isJoined=$isJoined")
                        },
                        onFailure = { exception ->
                            println("Error loading club info: ${exception.message}")
                            handleError(exception)
                        }
                    )
                } else {
                    println("No valid club ID found")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = ""
                    )
                }
            } catch (e: Exception) {
                println("Exception in getClubInfo: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unexpected error: ${e.message}"
                )
            }
        }
    }

    fun getClubsList(context: Context, limit: Int = 10, offset: Int = 0) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Get the current user ID
                val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val currentUserId = sharedPreferences.getString("user_id", "") ?: ""

                val repository = getClubRepository(context)
                val result = repository.getClubsList(limit, offset)

                result.fold(
                    onSuccess = { response ->
                        // Mark clubs as joined if the user is a member
                        val updatedClubs = response.data.map { club ->
                            club.copy(isJoined = club.members.contains(currentUserId))
                        }
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            clubsList = updatedClubs,
                            errorMessage = ""
                        )
                        
                        println("Clubs list loaded with ${updatedClubs.size} clubs")
                        updatedClubs.forEach { club ->
                            println("Club: ${club.name}, isJoined: ${club.isJoined}, members: ${club.members}")
                        }
                    },
                    onFailure = { exception ->
                        handleError(exception)
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Unexpected error: ${e.message}"
                )
            }
        }
    }

    fun createClub(context: Context) {
        val name = _uiState.value.clubName
        val description = _uiState.value.clubDescription

        if (name.isBlank() || description.isBlank()) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Club name and description are required",
                isSuccess = false
            )
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val repository = getClubRepository(context)
                val result = repository.createClub(name, description)

                result.fold(
                    onSuccess = { response ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            createdClub = response,
                            isSuccess = true,
                            errorMessage = ""
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isSuccess = false
                        )
                        handleError(exception)
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    errorMessage = "Error: ${e.message ?: "Failed to create club"}"
                )
            }
        }
    }

    fun addMember(context: Context, clubId: String, userId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val repository = getClubRepository(context)
                val result = repository.addMember(clubId, userId)

                result.fold(
                    onSuccess = { response ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            lastMemberOperation = response,
                            isSuccess = true,
                            errorMessage = ""
                        )

                        // Refresh club info after adding member
                        getClubInfo(context, clubId)
                        // Also refresh the clubs list to update join status
                        getClubsList(context)
                    },
                    onFailure = { exception ->
                        handleError(exception)
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error: ${e.message ?: "Unknown error"}"
                )
            }
        }
    }

    fun addMemberByUsername(context: Context, clubId: String, username: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val userRepository = UserRepository.getInstance(context)
                val userIdResult = userRepository.getUserIdByUsername(username)
                
                userIdResult.fold(
                    onSuccess = { response ->
                        val userId = response.user_id
                        addMember(context, clubId, userId)
                    },
                    onFailure = { exception ->
                        // Ignore errors and attempt to add with the username directly
                        // This handles the case where username doesn't exist but we want to proceed anyway
//                        addMember(context, clubId, username)
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Network error: ${exception.message ?: "Unknown error"}"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error: ${e.message ?: "Unknown error"}"
                )
            }
        }
    }

    fun removeMember(context: Context, clubId: String, userId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val repository = getClubRepository(context)
                val result = repository.removeMember(clubId, userId)

                result.fold(
                    onSuccess = { response ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            lastMemberOperation = response,
                            isSuccess = true,
                            errorMessage = ""
                        )

                        // Refresh club info after removing member
                        getClubInfo(context, clubId)
                    },
                    onFailure = { exception ->
                        handleError(exception)
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error: ${e.message ?: "Unknown error"}"
                )
            }
        }
    }

    // Function to allow a user to leave their current club
    fun leaveClub(context: Context, clubId: String) {
        // Get the current user ID from SharedPreferences
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val currentUserId = sharedPreferences.getString("user_id", "") ?: ""
        
        if (currentUserId.isNotBlank() && clubId.isNotBlank()) {
            removeMember(context, clubId, currentUserId)
            
            // After leaving, refresh both club info and club list
            viewModelScope.launch {
                // Add a small delay to ensure the remove member operation completes
                kotlinx.coroutines.delay(500)
                getClubsList(context)
                // Clear the current club info
                _uiState.value = _uiState.value.copy(clubInfo = null)
            }
        }
    }

    fun clearLastMemberOperation() {
        _uiState.value = _uiState.value.copy(
            lastMemberOperation = null
        )
    }

    fun handleError(exception: Throwable) {
        when (exception) {
            is HttpException -> {
                val errorMessage = when (exception.code()) {
                    400 -> "Invalid club data"
                    404 -> "Club not found"
                    500 -> "Server error"
                    else -> "HTTP error: ${exception.code()}"
                }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = errorMessage
                )
            }
            else -> {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error: ${exception.message ?: "Unknown error"}"
                )
            }
        }
    }

    data class ClubsUiState(
        val clubName: String = "",
        val clubDescription: String = "",
        val clubInfo: ClubInfoResponse? = null,
        val clubsList: List<ClubInfoResponse> = emptyList(),
        val createdClub: ClubCreateResponse? = null,
        val lastMemberOperation: ClubMemberResponse? = null,
        val errorMessage: String = "",
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false
    )
}

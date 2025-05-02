package com.ifmo.rmp.ui.screens.clubs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ifmo.rmp.data.model.ClubCreateResponse
import com.ifmo.rmp.data.model.ClubInfoResponse
import com.ifmo.rmp.data.repository.ClubRepository
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

    fun getClubInfo(context: Context, clubId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val repository = getClubRepository(context)
                val result = repository.getClubInfo(clubId)
                
                result.fold(
                    onSuccess = { clubInfo ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            clubInfo = clubInfo,
                            errorMessage = ""
                        )
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
    
    fun getClubsList(context: Context, limit: Int = 10, offset: Int = 0) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val repository = getClubRepository(context)
                val result = repository.getClubsList(limit, offset)
                
                result.fold(
                    onSuccess = { response ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            clubsList = response.data,
                            errorMessage = ""
                        )
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
            _uiState.value = _uiState.value.copy(errorMessage = "Club name and description are required")
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
    
    private fun handleError(exception: Throwable) {
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
                    errorMessage = "Network error: ${exception.message}"
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
        val errorMessage: String = "",
        val isLoading: Boolean = false,
        val isSuccess: Boolean = false
    )
}

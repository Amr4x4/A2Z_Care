package com.example.a2zcare.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.domain.entities.LocationData
import com.example.a2zcare.domain.entities.User
import com.example.a2zcare.domain.usecases.GetAvailableUsersUseCase
import com.example.a2zcare.domain.usecases.GetCurrentLocationUseCase
import com.example.a2zcare.domain.usecases.ShareLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LocationSharingUiState(
    val currentLocation: LocationData? = null,
    val availableUsers: List<User> = emptyList(),
    val isSharing: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class LocationSharingViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getAvailableUsersUseCase: GetAvailableUsersUseCase,
    private val shareLocationUseCase: ShareLocationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationSharingUiState())
    val uiState: StateFlow<LocationSharingUiState> = _uiState.asStateFlow()

    init {
        Log.d("LocationViewModel", "ViewModel initialized")
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            try {
                loadAvailableUsers()
                loadCurrentLocation()
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error in loadData", e)
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to initialize: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    private suspend fun loadCurrentLocation() {
        try {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            Log.d("LocationViewModel", "Starting location load")

            getCurrentLocationUseCase()
                .catch { exception ->
                    Log.e("LocationViewModel", "Error in location flow", exception)
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Location error: ${exception.message}",
                        isLoading = false
                    )
                }
                .collect { location ->
                    Log.d("LocationViewModel", "Location received: $location")
                    _uiState.value = _uiState.value.copy(
                        currentLocation = location,
                        isLoading = false,
                        errorMessage = if (location == null) "Location not available" else null
                    )
                }
        } catch (e: Exception) {
            Log.e("LocationViewModel", "Exception in loadCurrentLocation", e)
            _uiState.value = _uiState.value.copy(
                errorMessage = "Location exception: ${e.message}",
                isLoading = false
            )
        }
    }

    private suspend fun loadAvailableUsers() {
        try {
            Log.d("LocationViewModel", "Loading users")
            val users = getAvailableUsersUseCase()
            _uiState.value = _uiState.value.copy(availableUsers = users)
            Log.d("LocationViewModel", "Users loaded: ${users.size}")
        } catch (e: Exception) {
            Log.e("LocationViewModel", "Error loading users", e)
            _uiState.value = _uiState.value.copy(
                errorMessage = "Failed to load users: ${e.message}"
            )
        }
    }

    fun shareLocationWith(userId: String) {
        viewModelScope.launch {
            _uiState.value.currentLocation?.let { location ->
                try {
                    _uiState.value = _uiState.value.copy(isSharing = true)
                    shareLocationUseCase(userId, location)
                    _uiState.value = _uiState.value.copy(
                        isSharing = false,
                        errorMessage = null
                    )
                    Log.d("LocationViewModel", "Location shared successfully with user: $userId")
                } catch (e: Exception) {
                    Log.e("LocationViewModel", "Error sharing location", e)
                    _uiState.value = _uiState.value.copy(
                        isSharing = false,
                        errorMessage = "Failed to share location: ${e.message}"
                    )
                }
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "No location available to share"
                )
            }
        }
    }

    fun refreshLocation() {
        viewModelScope.launch {
            loadCurrentLocation()
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
package com.example.a2zcare.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.remote.response.LocationUser
import com.example.a2zcare.domain.entities.LocationData
import com.example.a2zcare.domain.usecases.GetAvailableUsersUseCase
import com.example.a2zcare.domain.usecases.GetCurrentLocationUseCase
import com.example.a2zcare.domain.usecases.SearchUserByUsernameUseCase
import com.example.a2zcare.domain.usecases.SendLocationViaEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LocationSharingUiState(
    val currentLocation: LocationData? = null,
    val availableUsers: List<LocationUser> = emptyList(),
    val searchResults: List<LocationUser> = emptyList(),
    val recentUsers: List<LocationUser> = emptyList(),
    val isSharing: Boolean = false,
    val isLoading: Boolean = true,
    val isSearching: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class LocationSharingViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val getAvailableUsersUseCase: GetAvailableUsersUseCase,
    private val searchUserByUsernameUseCase: SearchUserByUsernameUseCase,
    private val sendLocationViaEmailUseCase: SendLocationViaEmailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationSharingUiState())
    val uiState: StateFlow<LocationSharingUiState> = _uiState.asStateFlow()

    // In-memory storage for recent users (in production, use a database)
    private val _recentUsers = mutableSetOf<LocationUser>()

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
            _uiState.value = _uiState.value.copy(
                availableUsers = users,
                recentUsers = _recentUsers.toList()
            )
            Log.d("LocationViewModel", "Users loaded: ${users.size}")
        } catch (e: Exception) {
            Log.e("LocationViewModel", "Error loading users", e)
            _uiState.value = _uiState.value.copy(
                errorMessage = "Failed to load users: ${e.message}"
            )
        }
    }

    // Method called from UI - renamed from shareLocationWith to shareLocationWithUser
    fun shareLocationWithUser(user: LocationUser) {
        viewModelScope.launch {
            _uiState.value.currentLocation?.let { location ->
                try {
                    _uiState.value = _uiState.value.copy(isSharing = true, errorMessage = null, successMessage = null)

                    // Send location via email
                    sendLocationViaEmailUseCase(user.email, location)

                    // Add to recent users
                    _recentUsers.add(user)

                    _uiState.value = _uiState.value.copy(
                        isSharing = false,
                        successMessage = "Location shared successfully with ${user.userName}",
                        recentUsers = _recentUsers.toList()
                    )
                    Log.d("LocationViewModel", "Location shared successfully with user: ${user.userName}")
                } catch (e: Exception) {
                    Log.e("LocationViewModel", "Error sharing location", e)
                    _uiState.value = _uiState.value.copy(
                        isSharing = false,
                        errorMessage = "Failed to send location via email: ${e.message}"
                    )
                }
            } ?: run {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "No location available to share"
                )
            }
        }
    }

    // Method called from UI - renamed from searchUsers to onSearchQueryChange
    fun onSearchQueryChange(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                searchQuery = query,
                isSearching = true,
                errorMessage = null
            )

            if (query.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    searchResults = emptyList(),
                    isSearching = false
                )
                return@launch
            }

            try {
                // Search by username first
                val searchResult = searchUserByUsernameUseCase(query)
                val searchResults = if (searchResult != null) listOf(searchResult) else emptyList()

                // Also filter available users by email
                val emailFilteredUsers = _uiState.value.availableUsers.filter { user ->
                    user.email.contains(query, ignoreCase = true) &&
                            !searchResults.any { it.id == user.id }
                }

                _uiState.value = _uiState.value.copy(
                    searchResults = searchResults + emailFilteredUsers,
                    isSearching = false
                )
            } catch (e: Exception) {
                Log.e("LocationViewModel", "Error searching users", e)
                _uiState.value = _uiState.value.copy(
                    searchResults = emptyList(),
                    isSearching = false,
                    errorMessage = "Search failed: ${e.message}"
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

    fun clearSuccess() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}
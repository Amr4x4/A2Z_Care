package com.example.a2zcare.presentation.viewmodel
/*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.domain.entities.*
import com.example.a2zcare.domain.usecases.*
import com.example.a2zcare.presentation.ui_state_classes.LocationSharingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

@HiltViewModel
class LocationSharingViewModel @Inject constructor(
    private val getLocationUpdatesUseCase: GetLocationUpdatesUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val searchUsersUseCase: SearchUsersUseCase,
    private val shareLocationUseCase: ShareLocationUseCase,
    private val saveUserUseCase: SaveUserUseCase,
    private val getSavedUsersUseCase: GetSavedUsersUseCase,
    private val removeSavedUserUseCase: RemoveSavedUserUseCase,
    private val addEmergencyUserUseCase: AddEmergencyUserUseCase,
    private val getEmergencyUsersUseCase: GetEmergencyUsersUseCase,
    private val removeEmergencyUserUseCase: RemoveEmergencyUserUseCase,
    private val sendEmergencyAlertUseCase: SendEmergencyAlertUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocationSharingUiState())
    val uiState: StateFlow<LocationSharingUiState> = _uiState.asStateFlow()

    private val _currentLocation = MutableStateFlow<LocationData?>(null)
    val currentLocation: StateFlow<LocationData?> = _currentLocation.asStateFlow()

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> = _searchResults.asStateFlow()

    private val _savedUsers = MutableStateFlow<List<SavedUser>>(emptyList())
    val savedUsers: StateFlow<List<SavedUser>> = _savedUsers.asStateFlow()

    private val _emergencyUsers = MutableStateFlow<List<EmergencyUser>>(emptyList())
    val emergencyUsers: StateFlow<List<EmergencyUser>> = _emergencyUsers.asStateFlow()

    private val currentUserId = "current_user_id" // Replace with actual logic

    init {
        startLocationUpdates()
        loadSavedUsers()
        loadEmergencyUsers()
    }

    private fun startLocationUpdates() {
        viewModelScope.launch {
            getLocationUpdatesUseCase()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to get location updates"
                    )
                }
                .collect { location ->
                    _currentLocation.value = location
                }
        }
    }

    private fun loadSavedUsers() {
        viewModelScope.launch {
            getSavedUsersUseCase(currentUserId)
                .collect { users ->
                    _savedUsers.value = users
                }
        }
    }

    private fun loadEmergencyUsers() {
        viewModelScope.launch {
            getEmergencyUsersUseCase(currentUserId)
                .collect { users ->
                    _emergencyUsers.value = users
                }
        }
    }

    fun searchUsers(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = searchUsersUseCase(query)) {
                is Result.Success -> {
                    _searchResults.value = result.data
                    _uiState.value = _uiState.value.copy(isLoading = false, error = null)
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun shareLocation(targetUserId: String, message: String? = null) {
        val location = _currentLocation.value
        if (location == null) {
            _uiState.value = _uiState.value.copy(error = "Location not available")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = shareLocationUseCase(currentUserId, targetUserId, location, message)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Location shared successfully"
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun saveUser(targetUserId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = saveUserUseCase(currentUserId, targetUserId)) {
                is Result.Success -> {
                    loadSavedUsers()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "User saved successfully",
                        error = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun removeSavedUser(targetUserId: String) {
        viewModelScope.launch {
            when (val result = removeSavedUserUseCase(currentUserId, targetUserId)) {
                is Result.Success -> {
                    loadSavedUsers()
                    _uiState.value = _uiState.value.copy(
                        successMessage = "User removed from saved list",
                        error = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun addEmergencyUser(targetUserId: String) {
        viewModelScope.launch {
            when (val result = addEmergencyUserUseCase(currentUserId, targetUserId)) {
                is Result.Success -> {
                    loadEmergencyUsers()
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Emergency contact added",
                        error = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun removeEmergencyUser(targetUserId: String) {
        viewModelScope.launch {
            when (val result = removeEmergencyUserUseCase(currentUserId, targetUserId)) {
                is Result.Success -> {
                    loadEmergencyUsers()
                    _uiState.value = _uiState.value.copy(
                        successMessage = "Emergency contact removed",
                        error = null
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun sendEmergencyAlert(message: String) {
        val location = _currentLocation.value
        if (location == null) {
            _uiState.value = _uiState.value.copy(error = "Location not available")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = sendEmergencyAlertUseCase(currentUserId, location, message)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Emergency alert sent"
                    )
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }
}
 */

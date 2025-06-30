package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.model.User
import com.example.a2zcare.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Log

data class UserUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    // Keep track of last fetched username to avoid refetching
    private var lastFetchedUsername: String? = null

    fun fetchUser(username: String) {
        if (username.isBlank()) {
            Log.w("UserViewModel", "Username is empty, skipping fetch")
            return
        }

        // Avoid refetching the same user
        if (lastFetchedUsername == username && _uiState.value.user != null) {
            Log.d("UserViewModel", "User already loaded for: $username")
            return
        }

        Log.d("UserViewModel", "Fetching user: '$username'")
        lastFetchedUsername = username

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                userRepository.getUserByUsername(username.trim())
                    .onSuccess { user ->
                        Log.d("UserViewModel", "User fetched successfully: ${user.userName}, Email: ${user.email}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            user = user,
                            error = null
                        )
                    }
                    .onFailure { exception ->
                        Log.e("UserViewModel", "Failed to fetch user '$username': ${exception.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message
                        )
                    }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Exception in fetchUser: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearUser() {
        _uiState.value = UserUiState()
        lastFetchedUsername = null
    }
}


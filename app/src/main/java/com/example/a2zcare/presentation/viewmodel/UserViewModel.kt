package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.remote.request.UpdateUserRequest
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.domain.usecases.GetUserDataUseCase
import com.example.a2zcare.domain.usecases.UpdateUserUseCase
import com.example.a2zcare.presentation.ui_state_classes.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    // Add a separate StateFlow for first name to make it easier to observe
    private val _firstName = MutableStateFlow("")
    val firstName: StateFlow<String> = _firstName.asStateFlow()

    init {
        // Load user data when ViewModel is created
        getUserData()
    }

    fun getUserData() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                when (val result = getUserDataUseCase(userId)) {
                    is Result.Success -> {
                        val user = result.data
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            userData = user.toString()
                        )

                        // Extract first name with fallback logic
                        val displayName = when {
                            !user.firstName.isNullOrBlank() -> user.firstName
                            !user.name.isNullOrBlank() -> user.name
                            !user.userName.isNullOrBlank() -> user.userName
                            else -> "User"
                        }
                        _firstName.value = displayName
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                        _firstName.value = "User" // Fallback name
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "User not logged in"
                )
                _firstName.value = "User"
            }
        }
    }

    fun updateUser(request: UpdateUserRequest) {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                val params = UpdateUserUseCase.Params(userId, request)
                when (val result = updateUserUseCase(params)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            successMessage = "User updated successfully!"
                        )
                        // Refresh user data after update
                        getUserData()
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                    }
                }
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "User not logged in"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}
package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.remote.request.ResetPasswordRequest
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.domain.model.Result
import com.example.a2zcare.domain.usecases.ResetPasswordUseCase
import com.example.a2zcare.domain.usecases.ValidatePasswordUseCase
import com.example.a2zcare.presentation.ui_state_classes.ChangePasswordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _userEmail = MutableStateFlow("")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    init {
        loadUserEmail()
    }

    private fun loadUserEmail() {
        viewModelScope.launch {
            try {
                val email = tokenManager.getUserEmail()
                _userEmail.value = email ?: ""

                // If email is still empty, try to get from user data
                if (_userEmail.value.isEmpty()) {
                    val userData = tokenManager.getUserData()
                    _userEmail.value = userData?.email ?: ""
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = "Failed to load user information: ${e.message}")
                }
            }
        }
    }

    fun onCurrentPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                currentPassword = password,
                currentPasswordError = if (password.isBlank()) "Current password is required" else null
            )
        }
    }

    fun onNewPasswordChange(password: String) {
        _uiState.update {
            it.copy(newPassword = password)
        }
        validateNewPassword(password)

        if (_uiState.value.confirmPassword.isNotEmpty()) {
            validateConfirmPassword(_uiState.value.confirmPassword)
        }
    }

    fun onConfirmPasswordChange(password: String) {
        _uiState.update {
            it.copy(confirmPassword = password)
        }
        validateConfirmPassword(password)
    }

    private fun validateNewPassword(password: String) {
        val result = validatePasswordUseCase(password)
        _uiState.update {
            it.copy(newPasswordError = if (result.successful) null else result.errorMessage)
        }
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        val newPassword = _uiState.value.newPassword
        _uiState.update {
            it.copy(
                confirmPasswordError = when {
                    confirmPassword.isBlank() -> "Please confirm your new password"
                    confirmPassword != newPassword -> "Passwords do not match"
                    else -> null
                }
            )
        }
    }

    fun toggleCurrentPasswordVisibility() {
        _uiState.update {
            it.copy(currentPasswordVisible = !it.currentPasswordVisible)
        }
    }

    fun toggleNewPasswordVisibility() {
        _uiState.update {
            it.copy(newPasswordVisible = !it.newPasswordVisible)
        }
    }

    fun toggleConfirmPasswordVisibility() {
        _uiState.update {
            it.copy(confirmPasswordVisible = !it.confirmPasswordVisible)
        }
    }

    val isChangePasswordEnabled: StateFlow<Boolean> = uiState
        .map {
            it.currentPassword.isNotBlank() &&
                    it.newPassword.isNotBlank() &&
                    it.confirmPassword.isNotBlank() &&
                    it.currentPasswordError == null &&
                    it.newPasswordError == null &&
                    it.confirmPasswordError == null &&
                    !it.isLoading
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun changePassword(userEmail: String? = null) {
        val currentState = _uiState.value

        // Determine which email to use
        val emailToUse = when {
            !userEmail.isNullOrBlank() -> userEmail
            !_userEmail.value.isBlank() -> _userEmail.value
            else -> null
        }

        // Clear previous messages
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }

        // Validate email availability
        if (emailToUse.isNullOrBlank()) {
            _uiState.update {
                it.copy(errorMessage = "User email not found. Please try logging in again.")
            }
            return
        }

        // Validate form fields
        if (currentState.currentPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your current password") }
            return
        }

        if (currentState.newPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter a new password") }
            return
        }

        if (currentState.confirmPassword.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please confirm your new password") }
            return
        }

        if (currentState.newPassword != currentState.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "New passwords do not match") }
            return
        }

        // Validate password strength
        if (currentState.newPasswordError != null) {
            _uiState.update { it.copy(errorMessage = currentState.newPasswordError) }
            return
        }

        // Check if new password is same as current password
        if (currentState.currentPassword == currentState.newPassword) {
            _uiState.update { it.copy(errorMessage = "New password must be different from current password") }
            return
        }

        // Proceed with password change
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val request = ResetPasswordRequest(
                email = emailToUse,
                currentPassword = currentState.currentPassword,
                newPassword = currentState.newPassword
            )

            when (val result = resetPasswordUseCase.execute(request)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Password changed successfully!",
                            currentPassword = "",
                            newPassword = "",
                            confirmPassword = "",
                            errorMessage = null
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message ?: "Failed to change password"
                        )
                    }
                }

                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.update { it.copy(errorMessage = null, successMessage = null) }
    }

    fun setUserEmail(email: String) {
        if (email.isNotBlank()) {
            _userEmail.value = email
        }
    }

    fun refreshUserEmail() {
        loadUserEmail()
    }
}
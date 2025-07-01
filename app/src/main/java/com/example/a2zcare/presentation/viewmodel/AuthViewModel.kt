package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.model.LoginRequest
import com.example.a2zcare.data.model.RegisterRequest
import com.example.a2zcare.data.model.ResetPasswordRequest
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.domain.model.Result
import com.example.a2zcare.domain.usecases.ForgotPasswordUseCase
import com.example.a2zcare.domain.usecases.LoginUseCase
import com.example.a2zcare.domain.usecases.LogoutUseCase
import com.example.a2zcare.domain.usecases.RegisterUseCase
import com.example.a2zcare.domain.usecases.ResetPasswordUseCase
import com.example.a2zcare.domain.usecases.ValidateEmailUseCase
import com.example.a2zcare.domain.usecases.ValidatePasswordUseCase
import com.example.a2zcare.presentation.ui_state_classes.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            tokenManager.isLoggedIn().collect { isLoggedIn ->
                _uiState.value = _uiState.value.copy(isLoggedIn = isLoggedIn)
            }
        }
    }

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _confirmPasswordVisible = MutableStateFlow(false)
    val confirmPasswordVisible: StateFlow<Boolean> = _confirmPasswordVisible.asStateFlow()

    private val _agreedToTerms = MutableStateFlow(false)
    val agreedToTerms: StateFlow<Boolean> = _agreedToTerms.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()

    fun onUserNameChange(value: String) {
        _userName.value = value
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
        validateConfirmPassword(value)
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        _confirmPasswordError.value =
            if (confirmPassword == _password.value) null else "Passwords do not match"
    }

    fun onToggleConfirmPasswordVisibility() {
        _confirmPasswordVisible.value = !_confirmPasswordVisible.value
    }

    fun onAgreeToTermsChanged(checked: Boolean) {
        _agreedToTerms.value = checked
    }

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
        validateEmail(value)
    }

    fun onPasswordChange(value: String) {
        _password.value = value
        validatePassword(value)
    }

    fun onTogglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    private fun validateEmail(email: String) {
        val result = validateEmailUseCase(email)
        _emailError.value = if (result.successful) null else result.errorMessage
    }

    private fun validatePassword(password: String) {
        val result = validatePasswordUseCase(password)
        _passwordError.value = if (result.successful) null else result.errorMessage
    }


    val isSignUpEnabled = combine(
        combine(
            userName,
            email,
            password,
            confirmPassword
        ) { userName, email, password, confirmPassword ->
            userName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
        },
        combine(
            emailError,
            passwordError,
            confirmPasswordError
        ) { emailErr, passErr, confirmPassErr ->
            emailErr == null && passErr == null && confirmPassErr == null
        },
        agreedToTerms
    ) { fieldsValid, errorsValid, agreed ->
        fieldsValid && errorsValid && agreed
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun register(userName: String, email: String, password: String, role: Int = 0) {
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val request = RegisterRequest(userName, password, email, role)
            when (val result = registerUseCase(request)) {
                is Result.Success<Unit> -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Registration successful!"
                    )
                    _isLoading.value = false
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                    _isLoading.value = false
                }

                is Result.Loading -> {
                    _isLoading.value = result.isLoading
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }


    val isLoginEnabled = combine(
        combine(email, password) { email, password ->
            email.isNotBlank() && password.isNotBlank()
        },
        combine(emailError, passwordError) { emailErr, passErr ->
            emailErr == null && passErr == null
        }
    ) { fieldsValid, errorsValid ->
        fieldsValid && errorsValid
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun login(email: String, password: String, username: String = "") {
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val request = LoginRequest(email, password, username)
            when (val result = loginUseCase(request)) {
                is Result.Success<Unit> -> {
                    _isLoading.value = false
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        successMessage = "Login successful!"
                    )
                }

                is Result.Error -> {
                    _isLoading.value = false // Add this line
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }

                is Result.Loading -> {
                    _isLoading.value = result.isLoading
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = _uiState.value.copy(isLoading = true)

            when (val result = logoutUseCase(Unit)) {
                is Result.Success<Unit> -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = false,
                        successMessage = "Logged out successfully!"
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }

                is Result.Loading -> {
                    _isLoading.value = result.isLoading
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            when (val result = forgotPasswordUseCase(email)) {
                is Result.Success<Unit> -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Password reset email sent! Please check your email."
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }

                is Result.Loading -> {
                    _isLoading.value = result.isLoading
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun resetPassword(email: String, currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            val request = ResetPasswordRequest(email, currentPassword, newPassword)
            when (val result = resetPasswordUseCase(request)) {
                is Result.Success<Unit> -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "Password reset successful!"
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }

                is Result.Loading -> {
                    _isLoading.value = result.isLoading
                    _uiState.value = _uiState.value.copy(isLoading = result.isLoading)
                }
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMessage = null, successMessage = null)
    }
}
package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Patterns
import com.example.a2zcare.data.network.SignUpResponse
import com.example.a2zcare.data.repository.AuthRepository

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // Input fields
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    private val _confirmPasswordVisible = MutableStateFlow(false)
    val confirmPasswordVisible: StateFlow<Boolean> = _confirmPasswordVisible.asStateFlow()

    private val _agreedToTerms = MutableStateFlow(false)
    val agreedToTerms: StateFlow<Boolean> = _agreedToTerms.asStateFlow()

    // Error messages
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()

    // API loading & result
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _signUpResult = MutableSharedFlow<Result<SignUpResponse>>()
    val signUpResult = _signUpResult.asSharedFlow()

    // Validation logic regex for password:
    // at least 8 chars, at least one upper, one lower, one digit, one special char
    private val passwordPattern =
        Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$")

    fun onUserNameChange(value: String) {
        _userName.value = value
    }

    fun onEmailChange(value: String) {
        _email.value = value
        validateEmail(value)
    }

    private fun validateEmail(email: String) {
        _emailError.value = if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) null else "Invalid email format"
    }

    fun onPasswordChange(value: String) {
        _password.value = value
        validatePassword(value)
        validateConfirmPassword(_confirmPassword.value)
    }

    private fun validatePassword(password: String) {
        _passwordError.value = if (passwordPattern.matches(password)) null else
            "Password must be at least 8 characters and include uppercase, lowercase, number, and special character"
    }

    fun onConfirmPasswordChange(value: String) {
        _confirmPassword.value = value
        validateConfirmPassword(value)
    }

    private fun validateConfirmPassword(confirmPassword: String) {
        _confirmPasswordError.value = if (confirmPassword == _password.value) null else "Passwords do not match"
    }

    fun onTogglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun onToggleConfirmPasswordVisibility() {
        _confirmPasswordVisible.value = !_confirmPasswordVisible.value
    }

    fun onAgreeToTermsChanged(checked: Boolean) {
        _agreedToTerms.value = checked
    }

    // Enable SignUp button only if all validations pass
    val isSignUpEnabled = combine(
        emailError,
        passwordError,
        confirmPasswordError,
        agreedToTerms
    ) { emailErr, passErr, confirmPassErr, agreed ->
        emailErr == null && passErr == null && confirmPassErr == null && agreed
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun signUp() {
        if (!isSignUpEnabled.value) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.signUp(
                    userName = _userName.value,
                    email = _email.value,
                    password = _password.value
                )
                if (response.isSuccessful && response.body() != null) {
                    _signUpResult.emit(Result.success(response.body()!!))
                } else {
                    _signUpResult.emit(Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error")))
                }
            } catch (e: Exception) {
                _signUpResult.emit(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }
}

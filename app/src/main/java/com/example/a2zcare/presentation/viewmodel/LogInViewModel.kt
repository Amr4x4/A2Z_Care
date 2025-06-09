package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.network.LoginResponse
import com.example.a2zcare.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.util.Patterns

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible = _passwordVisible.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _loginResult = MutableSharedFlow<Result<LoginResponse>>()
    val loginResult = _loginResult.asSharedFlow()

    private val passwordPattern = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}\$")

    fun onEmailChange(value: String) {
        _email.value = value
        _emailError.value = if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) null else "Invalid email format"
    }

    fun onPasswordChange(value: String) {
        _password.value = value
        _passwordError.value = if (passwordPattern.matches(value)) null else
            "Password must be at least 8 characters with uppercase, lowercase, number, special char"
    }

    fun onTogglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    val isLoginEnabled = combine(emailError, passwordError) { emailErr, passErr ->
        emailErr == null && passErr == null
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun login() {
        if (!isLoginEnabled.value) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = authRepository.login(email.value, password.value)
                if (response.isSuccessful && response.body() != null) {
                    _loginResult.emit(Result.success(response.body()!!))
                } else {
                    _loginResult.emit(Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error")))
                }
            } catch (e: Exception) {
                _loginResult.emit(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }
}

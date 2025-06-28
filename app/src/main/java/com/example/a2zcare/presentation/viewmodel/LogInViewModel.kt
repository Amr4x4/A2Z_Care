package com.example.a2zcare.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.network.response.LoginResultResponse
import com.example.a2zcare.domain.model.NetworkResult
import com.example.a2zcare.domain.usecases.LoginUseCase
import com.example.a2zcare.domain.usecases.ValidateEmailUseCase
import com.example.a2zcare.domain.usecases.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

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

    private val _loginResult = MutableSharedFlow<Result<LoginResultResponse>>()
    val loginResult: SharedFlow<Result<LoginResultResponse>> = _loginResult.asSharedFlow()

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

    fun login() {
        if (!isLoginEnabled.value) return

        viewModelScope.launch {
            _isLoading.value = true

            when (val result = loginUseCase(
                email = _email.value,
                password = _password.value
            )) {
                is NetworkResult.Success -> {
                    result.data?.let { loginResponse ->
                        Log.d("LoginViewModel", "Login Success: $loginResponse")
                        _loginResult.emit(Result.success(loginResponse))
                    } ?: run {
                        Log.e("LoginViewModel", "Login Success but empty result")
                        _loginResult.emit(Result.failure(Exception("Empty response data")))
                    }
                }
                is NetworkResult.Error -> {
                    Log.e("LoginViewModel", "Login Error: ${result.message}")
                    _loginResult.emit(Result.failure(Exception(result.message ?: "Unknown error")))
                }
                is NetworkResult.Loading -> {
                    // Optional: handle loading if needed
                }
            }

            _isLoading.value = false
        }
    }
}

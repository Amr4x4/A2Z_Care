package com.example.a2zcare.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.network.response.SignUpResultResponse
import com.example.a2zcare.domain.model.NetworkResult
import com.example.a2zcare.domain.usecases.SignUpUseCase
import com.example.a2zcare.domain.usecases.ValidateEmailUseCase
import com.example.a2zcare.domain.usecases.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) : ViewModel() {

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

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError: StateFlow<String?> = _confirmPasswordError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _signUpResult = MutableSharedFlow<Result<SignUpResultResponse>>()
    val signUpResult = _signUpResult.asSharedFlow()

    fun onUserNameChange(value: String) {
        _userName.value = value
    }

    fun onEmailChange(value: String) {
        _email.value = value
        validateEmail(value)
    }

    private fun validateEmail(email: String) {
        val result = validateEmailUseCase(email)
        _emailError.value = if (result.successful) null else result.errorMessage
    }

    fun onPasswordChange(value: String) {
        _password.value = value
        validatePassword(value)
        validateConfirmPassword(_confirmPassword.value)
    }

    private fun validatePassword(password: String) {
        val result = validatePasswordUseCase(password)
        _passwordError.value = if (result.successful) null else result.errorMessage
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

    val isSignUpEnabled = combine(
        combine(userName, email, password, confirmPassword) { userName, email, password, confirmPassword ->
            userName.isNotBlank() && email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
        },
        combine(emailError, passwordError, confirmPasswordError) { emailErr, passErr, confirmPassErr ->
            emailErr == null && passErr == null && confirmPassErr == null
        },
        agreedToTerms
    ) { fieldsValid, errorsValid, agreed ->
        fieldsValid && errorsValid && agreed
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    fun signUp() {
        if (!isSignUpEnabled.value) return

        viewModelScope.launch {
            _isLoading.value = true

            when (val result = signUpUseCase(
                userName = _userName.value,
                email = _email.value,
                password = _password.value
            )) {
                is NetworkResult.Success -> {
                    result.data?.let { signUpResultData ->
                        Log.d("SignUpViewModel", "SignUp Success: $signUpResultData")
                        _signUpResult.emit(Result.success(signUpResultData))
                    } ?: run {
                        Log.e("SignUpViewModel", "SignUp Success but empty result")
                        _signUpResult.emit(Result.failure(Exception("Empty response data")))
                    }
                }
                is NetworkResult.Error -> {
                    Log.e("SignUpViewModel", "SignUp Error: ${result.message}")
                    _signUpResult.emit(Result.failure(Exception(result.message ?: "Unknown error")))
                }
                is NetworkResult.Loading -> {}
            }

            _isLoading.value = false
        }
    }
}

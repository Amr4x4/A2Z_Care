package com.example.a2zcare.presentation.viewmodel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SignUpViewModel : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _passwordVisible = MutableStateFlow(false)
    val passwordVisible: StateFlow<Boolean> = _passwordVisible.asStateFlow()

    private val _agreedToTerms = MutableStateFlow(false)
    val agreedToTerms: StateFlow<Boolean> = _agreedToTerms.asStateFlow()

    fun onEmailChange(value: String) {
        _email.value = value
    }

    fun onPasswordChange(value: String) {
        _password.value = value
    }

    fun onTogglePasswordVisibility() {
        _passwordVisible.value = !_passwordVisible.value
    }

    fun onAgreeToTermsChanged(checked: Boolean) {
        _agreedToTerms.value = checked
    }
}
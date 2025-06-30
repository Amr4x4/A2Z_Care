package com.example.a2zcare.presentation.viewmodel.forget_password_view_model

import androidx.lifecycle.ViewModel
import com.example.a2zcare.domain.usecases.ValidateEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectEmailViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private fun validateEmail(email: String) {
        val result = validateEmailUseCase(email)
        _emailError.value = if (result.successful) null else result.errorMessage
    }

    fun onEmailChange(value: String) {
        _email.value = value
        validateEmail(value)
    }
}
package com.example.a2zcare.presentation.viewmodel.forget_password_view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.network.response.ForgotPasswordResponse
import com.example.a2zcare.domain.model.NetworkResult
import com.example.a2zcare.domain.usecases.ForgetPasswordUseCase
import com.example.a2zcare.domain.usecases.ValidateEmailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectEmailViewModel @Inject constructor(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val forgetPasswordUseCase: ForgetPasswordUseCase
) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _forgotPasswordResult = MutableStateFlow<NetworkResult<ForgotPasswordResponse>?>(null)
    val forgotPasswordResult: StateFlow<NetworkResult<ForgotPasswordResponse>?> = _forgotPasswordResult.asStateFlow()

    private fun validateEmail(email: String) {
        val result = validateEmailUseCase(email)
        _emailError.value = if (result.successful) null else result.errorMessage
    }

    fun onEmailChange(value: String) {
        _email.value = value
        validateEmail(value)
    }

    fun sendForgetPasswordRequest() {
        if (_email.value.isNotEmpty() && _emailError.value == null) {
            viewModelScope.launch {
                Log.d("SelectEmailViewModel", "Sending forgot password request for email: ${_email.value}")
                _isLoading.value = true
                _forgotPasswordResult.value = null

                try {
                    val result = forgetPasswordUseCase(_email.value)
                    Log.d("SelectEmailViewModel", "Forgot password result: $result")

                    // In case the backend succeeded but threw an IOException, assume success for fallback
                    if (result is NetworkResult.Error && result.message?.contains("Connection error") == true) {
                        _forgotPasswordResult.value = NetworkResult.Success(
                            ForgotPasswordResponse(
                                success = true,
                                message = "OTP assumed sent (possible fallback)",
                                data = null
                            )
                        )
                    } else {
                        _forgotPasswordResult.value = result
                    }
                } catch (e: Exception) {
                    Log.e("SelectEmailViewModel", "Error sending forgot password request", e)
                    _forgotPasswordResult.value = NetworkResult.Error("Failed to send request: ${e.message}")
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    fun clearResult() {
        _forgotPasswordResult.value = null
    }
}
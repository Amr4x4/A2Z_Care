package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.model.EmergencyContactRequest
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.domain.usecases.CreateEmergencyContactUseCase
import com.example.a2zcare.domain.usecases.GetEmergencyContactsUseCase
import com.example.a2zcare.presentation.ui_state_classes.EmergencyContactUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

@HiltViewModel
class EmergencyContactViewModel @Inject constructor(
    private val createEmergencyContactUseCase: CreateEmergencyContactUseCase,
    private val getEmergencyContactsUseCase: GetEmergencyContactsUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(EmergencyContactUiState())
    val uiState: StateFlow<EmergencyContactUiState> = _uiState.asStateFlow()

    fun getEmergencyContacts() {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                when (val result = getEmergencyContactsUseCase(userId)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            contacts = result.data
                        )
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

    fun createEmergencyContact(request: EmergencyContactRequest) {
        viewModelScope.launch {
            val userId = tokenManager.getUserId()
            if (userId != null) {
                _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                val params = CreateEmergencyContactUseCase.Params(userId, request)
                when (val result = createEmergencyContactUseCase(params)) {
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            successMessage = "Emergency contact created successfully!"
                        )
                        // Refresh contacts after creation
                        getEmergencyContacts()
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
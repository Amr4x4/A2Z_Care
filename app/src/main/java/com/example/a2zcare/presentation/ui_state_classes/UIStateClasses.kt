package com.example.a2zcare.presentation.ui_state_classes

import com.example.a2zcare.data.model.UserWithEmergencyContacts

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

data class UserUiState(
    val isLoading: Boolean = false,
    val userData: String? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

data class EmergencyContactUiState(
    val isLoading: Boolean = false,
    val contacts: UserWithEmergencyContacts? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

data class HealthDataUiState(
    val isLoading: Boolean = false,
    val bloodPressureData: String? = null,
    val heartRateData: String? = null,
    val heartDiseaseData: String? = null,
    val activityData: String? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

data class MessagingUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
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

data class OnboardingUiState(
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val age: Int = 0,
    val height: Int = 0, // Changed from Double to Int
    val weight: Double = 0.0,
    val gender: String? = null,
    val activityLevel: String? = null,
    val healthGoals: String? = null,
    val calculatedStepsTarget: Int = 0,
    val calculatedCaloriesTarget: Int = 0,
    val isLoading: Boolean = false,
    val isCompleted: Boolean = false,
    val errorMessage: String? = null
) {
    fun isValid(): Boolean {
        return firstName.isNotBlank() &&
                lastName.isNotBlank() &&
                age > 0 &&
                height > 0 && // Now Int
                weight > 0 &&
                !gender.isNullOrEmpty() &&
                !activityLevel.isNullOrEmpty() &&
                !healthGoals.isNullOrEmpty()
    }
}

data class SensorDataUiState(
    val watchId: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val totalReadings: Int = 0
)
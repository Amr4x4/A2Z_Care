package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.data.remote.request.UpdateUserRequest
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.domain.model.Result
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import com.example.a2zcare.domain.repository.StepTrackerRepository
import com.example.a2zcare.domain.usecases.CalculateTargetsUseCase
import com.example.a2zcare.domain.entities.ActivityLevel
import com.example.a2zcare.domain.entities.CalorieIntakeType
import com.example.a2zcare.domain.entities.Gender
import com.example.a2zcare.domain.entities.UserProfile
import com.example.a2zcare.presentation.ui_state_classes.OnboardingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PersonalOnboardingViewModel @Inject constructor(
    private val repository: StepTrackerRepository,
    private val healthRepository: HealthMonitoringRepository,
    private val tokenManager: TokenManager,
    private val calculateTargetsUseCase: CalculateTargetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            try {
                val userProfile = repository.getUserProfile()
                if (userProfile != null) {
                    _uiState.value = _uiState.value.copy(isCompleted = true)
                }
            } catch (e: Exception) {
                // Handle error silently or log
            }
        }
    }

    // Basic info updates
    fun updateFirstName(firstName: String) {
        _uiState.value = _uiState.value.copy(firstName = firstName)
    }

    fun updateLastName(lastName: String) {
        _uiState.value = _uiState.value.copy(lastName = lastName)
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phoneNumber)
    }

    fun updateAge(age: Int) {
        _uiState.value = _uiState.value.copy(age = age)
        calculateTargets()
    }

    fun updateHeight(height: Int) {
        _uiState.value = _uiState.value.copy(height = height)
        calculateTargets()
    }

    fun updateWeight(weight: Double) {
        _uiState.value = _uiState.value.copy(weight = weight)
        calculateTargets()
    }

    fun updateGender(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender)
        calculateTargets()
    }

    // FIXED: This method was incorrectly updating dateOfBirth field with activity level
    fun updateActivityLevel(activityLevel: String) {
        _uiState.value = _uiState.value.copy(activityLevel = activityLevel) // Fixed field name
        calculateTargets()
    }

    fun updateHealthGoals(healthGoals: String) {
        _uiState.value = _uiState.value.copy(healthGoals = healthGoals)
        calculateTargets()
    }

    private fun calculateTargets() {
        val state = _uiState.value
        if (state.age > 0 && state.height > 0 && state.weight > 0 &&
            !state.gender.isNullOrEmpty() && !state.activityLevel.isNullOrEmpty() && !state.healthGoals.isNullOrEmpty()) {

            // Convert string values to enum for calculation
            val genderEnum = when(state.gender?.lowercase()) {
                "male" -> Gender.MALE
                "female" -> Gender.FEMALE
                else -> Gender.MALE
            }

            val activityEnum = when(state.activityLevel?.lowercase()?.replace(" ", "_")) {
                "sedentary" -> ActivityLevel.SEDENTARY
                "lightly_active" -> ActivityLevel.LIGHTLY_ACTIVE
                "moderately_active" -> ActivityLevel.MODERATELY_ACTIVE
                "very_active" -> ActivityLevel.VERY_ACTIVE
                "extremely_active" -> ActivityLevel.EXTREMELY_ACTIVE
                else -> ActivityLevel.MODERATELY_ACTIVE
            }

            val calorieIntakeEnum = when(state.healthGoals?.lowercase()) {
                "lose weight" -> CalorieIntakeType.WEIGHT_LOSS
                "gain weight" -> CalorieIntakeType.WEIGHT_GAIN
                "build muscle" -> CalorieIntakeType.MUSCLE_BUILDING
                "maintain current weight" -> CalorieIntakeType.MAINTENANCE
                else -> CalorieIntakeType.MAINTENANCE
            }

            val stepsTarget = calculateTargetsUseCase.calculateDailyStepsTarget(
                state.age, genderEnum, activityEnum, state.weight.toFloat(), state.height.toFloat()
            )

            val caloriesTarget = calculateTargetsUseCase.calculateDailyCaloriesGainTarget(
                state.age, genderEnum, state.weight.toFloat(), state.height.toFloat(),
                activityEnum, calorieIntakeEnum
            )

            _uiState.value = _uiState.value.copy(
                calculatedStepsTarget = stepsTarget,
                calculatedCaloriesTarget = caloriesTarget
            )
        }
    }

    fun saveUserProfile() {
        val state = _uiState.value
        if (state.isValid()) {
            viewModelScope.launch {
                try {
                    _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

                    val userId = tokenManager.getUserId()
                    if (userId.isNullOrEmpty()) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "User not found. Please login again."
                        )
                        return@launch
                    }

                    // FIXED: Generate proper date of birth from age
                    val dateOfBirth = generateDateOfBirthFromAge(state.age)

                    // Create update request with proper data mapping
                    val updateRequest = UpdateUserRequest(
                        id = userId,
                        firstName = state.firstName.takeIf { it.isNotBlank() },
                        lastName = state.lastName.takeIf { it.isNotBlank() },
                        name = "${state.firstName} ${state.lastName}".trim().takeIf { it.isNotBlank() },
                        phoneNumber = state.phoneNumber.takeIf { it.isNotBlank() },
                        address = null,
                        age = state.age.takeIf { it > 0 },
                        dateOfBirth = dateOfBirth,
                        gender = state.gender?.takeIf { it.isNotBlank() },
                        weightKg = state.weight.takeIf { it > 0 },
                        height = state.height.takeIf { it > 0 }, // Now Int
                        healthGoals = state.healthGoals?.takeIf { it.isNotBlank() },
                        updatedAt = getCurrentTimestamp()
                    )

                    // Debug logging
                    println("UpdateUserRequest: $updateRequest")

                    // Call API to update user data
                    when (val result = healthRepository.updateUser(userId, updateRequest)) {
                        is Result.Success -> {
                            // You can use result.data (UpdateUserResponse) here if needed
                            // Convert string values back to enums for local storage
                            val genderEnum = when(state.gender?.lowercase()) {
                                "male" -> Gender.MALE
                                "female" -> Gender.FEMALE
                                else -> Gender.MALE
                            }

                            val activityEnum = when(state.activityLevel?.lowercase()?.replace(" ", "_")) {
                                "sedentary" -> ActivityLevel.SEDENTARY
                                "lightly_active" -> ActivityLevel.LIGHTLY_ACTIVE
                                "moderately_active" -> ActivityLevel.MODERATELY_ACTIVE
                                "very_active" -> ActivityLevel.VERY_ACTIVE
                                "extremely_active" -> ActivityLevel.EXTREMELY_ACTIVE
                                else -> ActivityLevel.MODERATELY_ACTIVE
                            }

                            val calorieIntakeEnum = when(state.healthGoals?.lowercase()) {
                                "lose weight" -> CalorieIntakeType.WEIGHT_LOSS
                                "gain weight" -> CalorieIntakeType.WEIGHT_GAIN
                                "build muscle" -> CalorieIntakeType.MUSCLE_BUILDING
                                "maintain current weight" -> CalorieIntakeType.MAINTENANCE
                                else -> CalorieIntakeType.MAINTENANCE
                            }

                            val userProfile = UserProfile(
                                id = "user_default",
                                age = state.age,
                                height = state.height.toFloat(),
                                weight = state.weight.toFloat(),
                                gender = genderEnum,
                                activityLevel = activityEnum,
                                calorieIntakeType = calorieIntakeEnum,
                                dailyStepsTarget = state.calculatedStepsTarget,
                                dailyCaloriesBurnTarget = state.calculatedCaloriesTarget,
                                createdAt = System.currentTimeMillis()
                            )

                            repository.saveUserProfile(userProfile)
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                isCompleted = true
                            )
                        }
                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                errorMessage = "Failed to update profile: ${result.message}"
                            )
                        }
                        is Result.Loading -> {
                            // Handle loading state if needed
                        }
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "An error occurred: ${e.message}"
                    )
                }
            }
        }
    }

    // FIXED: Helper function to generate date of birth from age
    private fun generateDateOfBirthFromAge(age: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -age)
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(calendar.time)
    }

    // FIXED: Helper function to get current timestamp
    private fun getCurrentTimestamp(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date())
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
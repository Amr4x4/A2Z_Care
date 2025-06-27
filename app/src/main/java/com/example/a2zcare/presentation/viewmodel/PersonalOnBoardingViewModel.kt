package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.domain.entities.ActivityLevel
import com.example.a2zcare.domain.entities.CalorieIntakeType
import com.example.a2zcare.domain.entities.Gender
import com.example.a2zcare.domain.entities.UserProfile
import com.example.a2zcare.domain.repository.StepTrackerRepository
import com.example.a2zcare.domain.usecases.CalculateTargetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalOnboardingViewModel @Inject constructor(
    private val repository: StepTrackerRepository,
    private val calculateTargetsUseCase: CalculateTargetsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    fun updateAge(age: Int) {
        _uiState.value = _uiState.value.copy(age = age)
        calculateTargets()
    }

    fun updateHeight(height: Float) {
        _uiState.value = _uiState.value.copy(height = height)
        calculateTargets()
    }

    fun updateWeight(weight: Float) {
        _uiState.value = _uiState.value.copy(weight = weight)
        calculateTargets()
    }

    fun updateGender(gender: Gender) {
        _uiState.value = _uiState.value.copy(gender = gender)
        calculateTargets()
    }

    fun updateActivityLevel(activityLevel: ActivityLevel) {
        _uiState.value = _uiState.value.copy(activityLevel = activityLevel)
        calculateTargets()
    }

    fun updateCalorieIntakeType(calorieIntakeType: CalorieIntakeType) {
        _uiState.value = _uiState.value.copy(calorieIntakeType = calorieIntakeType)
        calculateTargets()
    }

    private fun calculateTargets() {
        val state = _uiState.value
        if (state.age > 0 && state.height > 0 && state.weight > 0 &&
            state.gender != null && state.activityLevel != null && state.calorieIntakeType != null) {

            val stepsTarget = calculateTargetsUseCase.calculateDailyStepsTarget(
                state.age, state.gender, state.activityLevel, state.weight, state.height
            )

            val caloriesTarget = calculateTargetsUseCase.calculateDailyCaloriesBurnTarget(
                state.age, state.gender, state.weight, state.height,
                state.activityLevel, state.calorieIntakeType
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
                    val userProfile = UserProfile(
                        id = "user_default",
                        age = state.age,
                        height = state.height,
                        weight = state.weight,
                        gender = state.gender!!,
                        activityLevel = state.activityLevel!!,
                        calorieIntakeType = state.calorieIntakeType!!,
                        dailyStepsTarget = state.calculatedStepsTarget,
                        dailyCaloriesBurnTarget = state.calculatedCaloriesTarget,
                        createdAt = System.currentTimeMillis()
                    )

                    repository.saveUserProfile(userProfile)
                    _uiState.value = _uiState.value.copy(isCompleted = true)
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
}

data class OnboardingUiState(
    val age: Int = 0,
    val height: Float = 0f,
    val weight: Float = 0f,
    val gender: Gender? = null,
    val activityLevel: ActivityLevel? = null,
    val calorieIntakeType: CalorieIntakeType? = null,
    val calculatedStepsTarget: Int = 0,
    val calculatedCaloriesTarget: Int = 0,
    val isCompleted: Boolean = false
) {
    fun isValid(): Boolean {
        return age > 0 && height > 0 && weight > 0 &&
                gender != null && activityLevel != null && calorieIntakeType != null
    }
}
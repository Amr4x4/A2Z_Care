package com.example.a2zcare.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.a2zcare.domain.repository.StepTrackerRepository
import com.example.a2zcare.domain.usecases.StepTrackingUseCase
import com.example.a2zcare.domain.entities.UserProfile
import com.example.a2zcare.domain.entities.StepDataTracker

@HiltViewModel
class StepsTrackingViewModel @Inject constructor(
    private val repository: StepTrackerRepository,
    private val stepTrackingUseCase: StepTrackingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _errorState.value = null

            try {
                val userProfile = repository.getUserProfile()
                val todayData = stepTrackingUseCase.getTodaySteps()
                val weeklyData = stepTrackingUseCase.getWeeklySteps()

                _uiState.value = _uiState.value.copy(
                    userProfile = userProfile,
                    todaySteps = todayData?.steps ?: 0,
                    todayCaloriesBurned = todayData?.caloriesBurned ?: 0,
                    todayDistance = todayData?.distanceKm ?: 0f,
                    todayActiveMinutes = todayData?.activeMinutes ?: 0,
                    weeklyStepData = weeklyData,
                    isLoading = false,
                    error = null
                )

                Log.d("StepsTrackingViewModel", "Data loaded successfully - Steps: ${todayData?.steps ?: 0}")
            } catch (e: Exception) {
                Log.e("StepsTrackingViewModel", "Error loading data", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
                _errorState.value = "Failed to load step data"
            }
        }
    }

    fun refreshData() {
        loadData()
    }

    fun clearError() {
        _errorState.value = null
    }
}

data class MainUiState(
    val userProfile: UserProfile? = null,
    val todaySteps: Int = 0,
    val todayCaloriesBurned: Int = 0,
    val todayDistance: Float = 0f,
    val todayActiveMinutes: Int = 0,
    val weeklyStepData: List<StepDataTracker> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
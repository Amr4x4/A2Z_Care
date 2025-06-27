package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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

    fun loadData() {
        viewModelScope.launch {
            try {
                val userProfile = repository.getUserProfile()
                val today = getCurrentDate()
                val todayData = repository.getStepDataByDate(today)
                val weeklyData = repository.getLastSevenDays()

                _uiState.value = _uiState.value.copy(
                    userProfile = userProfile,
                    todaySteps = todayData?.steps ?: 0,
                    todayCaloriesBurned = todayData?.caloriesBurned ?: 0,
                    todayDistance = todayData?.distanceKm ?: 0f,
                    todayActiveMinutes = todayData?.activeMinutes ?: 0,
                    weeklyStepData = weeklyData,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
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

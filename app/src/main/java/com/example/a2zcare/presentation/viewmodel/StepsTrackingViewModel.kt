package com.example.a2zcare.presentation.viewmodel

import android.util.Log
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

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _lastRefreshTime = MutableStateFlow(0L)
    val lastRefreshTime: StateFlow<Long> = _lastRefreshTime.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            _errorState.value = null

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
        viewModelScope.launch {
            _isRefreshing.value = true
            _errorState.value = null

            try {
                loadData()
                _lastRefreshTime.value = System.currentTimeMillis()
                Log.d("StepsTrackingViewModel", "Data refreshed successfully")
            } catch (e: Exception) {
                Log.e("StepsTrackingViewModel", "Error refreshing data", e)

                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "No internet connection"
                    is java.net.SocketTimeoutException -> "Request timed out"
                    is IllegalStateException -> "App is not ready"
                    else -> "Failed to refresh data"
                }
                _errorState.value = errorMessage
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun isRefreshNeeded(): Boolean {
        val currentTime = System.currentTimeMillis()
        val lastRefresh = _lastRefreshTime.value
        val refreshInterval = 5 * 60 * 1000L // 5 minutes
        return currentTime - lastRefresh > refreshInterval
    }

    fun forceRefresh() {
        if (!_isRefreshing.value) {
            refreshData()
        }
    }

    fun clearError() {
        _errorState.value = null
    }

    fun addSteps(steps: Int) {
        viewModelScope.launch {
            try {
                val currentSteps = _uiState.value.todaySteps
                _uiState.value = _uiState.value.copy(
                    todaySteps = currentSteps + steps
                )

                // Update in repository if needed
                stepTrackingUseCase.updateStepCount(steps)

                Log.d("StepsTrackingViewModel", "Added $steps steps. Total: ${_uiState.value.todaySteps}")
            } catch (e: Exception) {
                Log.e("StepsTrackingViewModel", "Error adding steps", e)
                _errorState.value = "Failed to add steps"
            }
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("StepsTrackingViewModel", "ViewModel cleared")
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
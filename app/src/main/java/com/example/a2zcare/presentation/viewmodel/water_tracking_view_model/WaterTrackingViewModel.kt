package com.example.a2zcare.presentation.viewmodel.water_tracking_view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.domain.entities.DailyWaterGoal
import com.example.a2zcare.domain.entities.GlassSize
import com.example.a2zcare.domain.entities.Interval
import com.example.a2zcare.domain.usecases.AddWaterIntakeUseCase
import com.example.a2zcare.domain.usecases.GetDailyProgressUseCase
import com.example.a2zcare.domain.usecases.GetIntervalsUseCase
import com.example.a2zcare.domain.usecases.UpdateIntervalUseCase
import com.example.a2zcare.notifications.water_tracking_notify.NotificationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaterTrackingViewModel @Inject constructor(
    private val addWaterIntakeUseCase: AddWaterIntakeUseCase,
    private val getDailyProgressUseCase: GetDailyProgressUseCase,
    private val getIntervalsUseCase: GetIntervalsUseCase,
    private val updateIntervalUseCase: UpdateIntervalUseCase,
    private val notificationManager: NotificationManager
) : ViewModel() {

    private val _dailyProgress = MutableStateFlow(
        DailyWaterGoal(
            date = "",
            targetAmount = 2000,
            achievedAmount = 0,
            goalPercentage = 0f
        )
    )
    val dailyProgress: StateFlow<DailyWaterGoal> = _dailyProgress.asStateFlow()

    private val _intervals = MutableStateFlow<List<Interval>>(emptyList())
    val intervals: StateFlow<List<Interval>> = _intervals.asStateFlow()

    private val _glassCount = MutableStateFlow(0)
    val glassCount: StateFlow<Int> = _glassCount.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Consistent StateFlow properties for refresh functionality
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    private val _lastRefreshTime = MutableStateFlow(0L)
    val lastRefreshTime: StateFlow<Long> = _lastRefreshTime.asStateFlow()

    init {
        loadDailyProgress()
        loadIntervals()
    }

    fun addWaterIntake(glassSize: GlassSize) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorState.value = null

            try {
                addWaterIntakeUseCase(glassSize)
                _glassCount.value += 1
                loadDailyProgress()
            } catch (e: Exception) {
                Log.e("WaterTrackingViewModel", "Error adding water intake", e)
                _errorState.value = "Failed to add water intake"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun incrementGlassCountNow() {
        addWaterIntake(GlassSize.MEDIUM)
    }

    fun onIntervalClick(minute: Int) {
        viewModelScope.launch {
            try {
                val updatedIntervals = _intervals.value.map { interval ->
                    interval.copy(selected = interval.minute == minute)
                }
                _intervals.value = updatedIntervals

                val selectedInterval = updatedIntervals.find { it.selected }
                selectedInterval?.let { interval ->
                    updateIntervalUseCase(interval)
                    notificationManager.scheduleWaterReminder(interval.minute)
                }
            } catch (e: Exception) {
                Log.e("WaterTrackingViewModel", "Error updating interval", e)
                _errorState.value = "Failed to update reminder interval"
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _errorState.value = null

            try {
                loadDailyProgress()
                loadIntervals()
                _lastRefreshTime.value = System.currentTimeMillis()
                Log.d("WaterTrackingViewModel", "Data refreshed successfully")
            } catch (e: Exception) {
                Log.e("WaterTrackingViewModel", "Error refreshing data", e)

                // Update error state with user-friendly message
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

    // Public function to manually trigger refresh
    fun forceRefresh() {
        if (!_isRefreshing.value) {
            refreshData()
        }
    }

    // Clear error state
    fun clearError() {
        _errorState.value = null
    }

    private fun loadDailyProgress() {
        viewModelScope.launch {
            try {
                val progress = getDailyProgressUseCase()
                _dailyProgress.value = progress
                _glassCount.value = progress.achievedAmount / 250 // Assuming 250ml per glass
            } catch (e: Exception) {
                Log.e("WaterTrackingViewModel", "Error loading daily progress", e)
                throw e // Re-throw to be handled by caller
            }
        }
    }

    private fun loadIntervals() {
        viewModelScope.launch {
            try {
                val intervalsList = getIntervalsUseCase()
                _intervals.value = intervalsList

                // Schedule notification based on selected interval
                val selectedInterval = intervalsList.find { it.selected }
                selectedInterval?.let {
                    notificationManager.scheduleWaterReminder(it.minute)
                }
            } catch (e: Exception) {
                Log.e("WaterTrackingViewModel", "Error loading intervals", e)
                throw e // Re-throw to be handled by caller
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("WaterTrackingViewModel", "ViewModel cleared")
    }
}
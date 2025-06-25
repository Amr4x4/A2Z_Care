package com.example.a2zcare.presentation.viewmodel.water_tracking_view_model


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

    init {
        loadDailyProgress()
        loadIntervals()
    }

    fun addWaterIntake(glassSize: GlassSize) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                addWaterIntakeUseCase(glassSize)
                _glassCount.value += 1
                loadDailyProgress()
            } catch (e: Exception) {
                // Handle error
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
                // Handle error
            }
        }
    }

    private fun loadDailyProgress() {
        viewModelScope.launch {
            try {
                val progress = getDailyProgressUseCase()
                _dailyProgress.value = progress
                _glassCount.value = progress.achievedAmount / 250 // Assuming 250ml per glass
            } catch (e: Exception) {
                // Handle error
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
                // Handle error
            }
        }
    }
}
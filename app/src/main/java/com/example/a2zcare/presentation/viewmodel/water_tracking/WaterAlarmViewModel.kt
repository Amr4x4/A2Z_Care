package com.example.a2zcare.presentation.viewmodel.water_tracking

import android.app.Application
import androidx.lifecycle.*
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.a2zcare.data.local.entity.Interval
import com.example.a2zcare.domain.usecases.*
import com.example.a2zcare.presentation.screens.notification.ReminderWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class WaterAlarmViewModel(
    application: Application,
    private val getGlassCount: GetGlassCountUseCase,
    private val incrementGlassCount: IncrementGlassCountUseCase,
    private val getReminderInterval: GetReminderIntervalUseCase,
    private val setReminderInterval: SetReminderIntervalUseCase
) : AndroidViewModel(application) {

    private val _glassCount = MutableLiveData(0)
    val glassCount: LiveData<Int> = _glassCount

    private val _intervals = MutableLiveData<List<Interval>>(emptyList())
    val intervals: LiveData<List<Interval>> = _intervals

    private val defaultMinutes = listOf(15, 30, 60)

    init {
        viewModelScope.launch {
            _glassCount.value = getGlassCount()
            val savedInterval = getReminderInterval()
            onIntervalClick(savedInterval, userClicked = false)
        }
    }

    fun onIntervalClick(minute: Int, userClicked: Boolean = true) {
        viewModelScope.launch {
            setReminderInterval(minute)
            if (userClicked) scheduleReminder(minute)
            _intervals.value = defaultMinutes.map { Interval(it, it == minute) }
        }
    }

    private fun scheduleReminder(minute: Int) {
        val workManager = WorkManager.getInstance(getApplication())
        workManager.cancelAllWorkByTag("hydration_reminder")
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(minute.toLong(), TimeUnit.MINUTES)
            .addTag("hydration_reminder")
            .build()
        workManager.enqueue(request)
    }

    fun incrementGlassCountNow() = viewModelScope.launch {
        incrementGlassCount()
        _glassCount.value = getGlassCount()
    }
}

package com.example.a2zcare.presentation.viewmodel.water_tracking

import android.app.Application
import androidx.lifecycle.*
import com.example.a2zcare.data.local.entity.Interval
import com.example.a2zcare.domain.usecases.GetGlassCountUseCase
import com.example.a2zcare.domain.usecases.GetReminderIntervalUseCase
import com.example.a2zcare.domain.usecases.IncrementGlassCountUseCase
import com.example.a2zcare.domain.usecases.SetReminderIntervalUseCase
import com.example.a2zcare.worker.ReminderWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.launch

class WaterAlarmViewModel(
    application: Application,
    private val getGlassCount: GetGlassCountUseCase,
    private val incrementGlassCount: IncrementGlassCountUseCase,
    private val getReminderInterval: GetReminderIntervalUseCase,
    private val setReminderInterval: SetReminderIntervalUseCase
) : AndroidViewModel(application) {

    private val _glassCount = MutableLiveData(0)
    val glassCount: LiveData<Int> = _glassCount

    private val _intervals = MutableLiveData<List<Interval>>(listOf())
    val intervals: LiveData<List<Interval>> = _intervals

    private val defaultMinutes = listOf(15, 30, 60)

    init {
        viewModelScope.launch {
            val interval = getReminderInterval()
            onIntervalClick(interval, userClicked = false)
            _glassCount.value = getGlassCount()
        }
    }

    fun onIntervalClick(minute: Int, userClicked: Boolean = true) {
        viewModelScope.launch {
            setReminderInterval(minute)
            if (userClicked) scheduleReminders(minute)
            _intervals.value = defaultMinutes.map { Interval(it, it == minute) }
        }
    }

    private fun scheduleReminders(minute: Int) {
        val workManager = WorkManager.getInstance(getApplication())
        workManager.cancelAllWorkByTag("hydration_reminder")

        val reminderRequest = PeriodicWorkRequestBuilder<ReminderWorker>(minute.toLong(), TimeUnit.MINUTES)
            .addTag("water_reminder")
            .build()

        workManager.enqueue(reminderRequest)
    }

    fun refreshGlassCount() = viewModelScope.launch {
        _glassCount.value = getGlassCount()
    }

    fun incrementGlassCountNow() = viewModelScope.launch {
        incrementGlassCount()
        _glassCount.value = getGlassCount()
    }
}

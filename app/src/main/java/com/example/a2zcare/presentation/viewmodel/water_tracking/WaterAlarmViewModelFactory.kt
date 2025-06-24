package com.example.a2zcare.presentation.viewmodel.water_tracking

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a2zcare.data.local.WaterTrackingStoringData
import com.example.a2zcare.domain.usecases.*

class WaterAlarmViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = WaterTrackingStoringData(application)
        return WaterAlarmViewModel(
            application,
            GetGlassCountUseCase(repo),
            IncrementGlassCountUseCase(repo),
            GetReminderIntervalUseCase(repo),
            SetReminderIntervalUseCase(repo)
        ) as T
    }
}

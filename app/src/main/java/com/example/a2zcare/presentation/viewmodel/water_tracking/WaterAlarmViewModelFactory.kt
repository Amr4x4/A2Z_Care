package com.example.a2zcare.presentation.viewmodel.water_tracking

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.a2zcare.data.local.WaterTrackingStoringData
import com.example.a2zcare.domain.usecases.GetGlassCountUseCase
import com.example.a2zcare.domain.usecases.GetReminderIntervalUseCase
import com.example.a2zcare.domain.usecases.IncrementGlassCountUseCase
import com.example.a2zcare.domain.usecases.SetReminderIntervalUseCase


class WaterAlarmViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = WaterTrackingStoringData(application)
        val viewModel = WaterAlarmViewModel(
            application,
            GetGlassCountUseCase(repository),
            IncrementGlassCountUseCase(repository),
            GetReminderIntervalUseCase(repository),
            SetReminderIntervalUseCase(repository)
        )
        return viewModel as T
    }
}
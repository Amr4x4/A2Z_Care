package com.example.a2zcare.domain.usecases

import com.example.a2zcare.domain.repository.WaterTrackingRepo


class GetGlassCountUseCase(private val repository: WaterTrackingRepo) {
    suspend operator fun invoke() = repository.getGlassCount()
}

class IncrementGlassCountUseCase(private val repository: WaterTrackingRepo) {
    suspend operator fun invoke() = repository.incrementGlassCount()
}

class GetReminderIntervalUseCase(private val repository: WaterTrackingRepo) {
    suspend operator fun invoke() = repository.getReminderInterval()
}

class SetReminderIntervalUseCase(private val repository: WaterTrackingRepo) {
    suspend operator fun invoke(minute: Int) = repository.setReminderInterval(minute)
}
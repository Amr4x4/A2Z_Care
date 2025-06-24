package com.example.a2zcare.domain.usecases

import com.example.a2zcare.domain.repository.WaterTrackingRepo

class GetGlassCountUseCase(private val repo: WaterTrackingRepo) {
    suspend operator fun invoke() = repo.getGlassCount()
}

class IncrementGlassCountUseCase(private val repo: WaterTrackingRepo) {
    suspend operator fun invoke() = repo.incrementGlassCount()
}

class GetReminderIntervalUseCase(private val repo: WaterTrackingRepo) {
    suspend operator fun invoke() = repo.getReminderInterval()
}

class SetReminderIntervalUseCase(private val repo: WaterTrackingRepo) {
    suspend operator fun invoke(minute: Int) = repo.setReminderInterval(minute)
}

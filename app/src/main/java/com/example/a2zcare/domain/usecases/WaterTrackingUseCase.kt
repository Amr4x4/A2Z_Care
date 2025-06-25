package com.example.a2zcare.domain.usecases


import com.example.a2zcare.domain.entities.DailyWaterGoal
import com.example.a2zcare.domain.entities.GlassSize
import com.example.a2zcare.domain.entities.Interval
import com.example.a2zcare.domain.entities.WaterIntakeEntry
import com.example.a2zcare.domain.repository.WaterTrackingRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AddWaterIntakeUseCase @Inject constructor(
    private val repository: WaterTrackingRepository
) {
    suspend operator fun invoke(glassSize: GlassSize) {
        val entry = WaterIntakeEntry(
            timestamp = System.currentTimeMillis(),
            amount = glassSize.ml,
            glassSize = glassSize
        )
        repository.addWaterIntake(entry)
    }
}

class GetDailyProgressUseCase @Inject constructor(
    private val repository: WaterTrackingRepository
) {
    suspend operator fun invoke(date: String = getCurrentDate()): DailyWaterGoal {
        return repository.getDailyGoal(date)
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}

class GetIntervalsUseCase @Inject constructor(
    private val repository: WaterTrackingRepository
) {
    suspend operator fun invoke(): List<Interval> {
        return repository.getAllIntervals()
    }
}

class UpdateIntervalUseCase @Inject constructor(
    private val repository: WaterTrackingRepository
) {
    suspend operator fun invoke(interval: Interval) {
        repository.updateInterval(interval)
    }
}
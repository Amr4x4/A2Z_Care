package com.example.a2zcare.domain.usecases

import com.example.a2zcare.domain.entities.StepDataTracker
import com.example.a2zcare.domain.repository.StepTrackerRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StepTrackingUseCase(
    private val repository: StepTrackerRepository,
    private val calculateTargetsUseCase: CalculateTargetsUseCase
) {

    suspend fun updateStepCount(steps: Int) {
        val today = getCurrentDate()
        val userId = "user_default" // In real app, get from auth

        val existingData = repository.getStepDataByDate(today)

        if (existingData != null) {
            val caloriesBurned = calculateCaloriesBurned(steps)
            repository.updateStepCount(today, steps, caloriesBurned)
        } else {
            val stepData = StepDataTracker(
                id = "${userId}_$today",
                userId = userId,
                date = today,
                steps = steps,
                caloriesBurned = calculateCaloriesBurned(steps),
                distanceKm = calculateDistance(steps),
                activeMinutes = calculateActiveMinutes(steps),
                lastUpdated = System.currentTimeMillis()
            )
            repository.saveStepData(stepData)
        }
    }

    private fun calculateCaloriesBurned(steps: Int): Int {
        // Approximate: 1 step = 0.04 calories for average person
        return (steps * 0.04).toInt()
    }

    private fun calculateDistance(steps: Int): Float {
        // Average step length: 0.762 meters
        return (steps * 0.762f) / 1000f // Convert to kilometers
    }

    private fun calculateActiveMinutes(steps: Int): Int {
        // Approximate: 100 steps per minute during active walking
        return steps / 100
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}

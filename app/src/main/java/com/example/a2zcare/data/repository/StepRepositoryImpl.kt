package com.example.a2zcare.data.repository

import com.example.a2zcare.domain.entities.StepData
import com.example.a2zcare.domain.repository.StepRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StepRepositoryImpl @Inject constructor() : StepRepository {

    private var todaySteps = 0
    private var todayCalories = 0.0

    override suspend fun getTodaySteps(): Flow<StepData?> = flow {
        val today = Calendar.getInstance().time
        emit(
            StepData(
                date = today,
                steps = todaySteps,
                caloriesBurned = todayCalories,
                startLocation = null,
                endLocation = null
            )
        )
    }

    override suspend fun saveStepData(stepData: StepData) {
        todaySteps = stepData.steps
        todayCalories = stepData.caloriesBurned
    }

    override suspend fun getStepHistory(): Flow<List<StepData>> = flow {
        emit(emptyList()) // Implement with Room database
    }

    fun updateSteps(steps: Int) {
        todaySteps = steps
        todayCalories = steps * 0.04 // Rough calculation
    }
}
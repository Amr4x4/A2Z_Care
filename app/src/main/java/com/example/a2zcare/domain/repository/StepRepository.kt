package com.example.a2zcare.domain.repository

import com.example.a2zcare.domain.entities.StepData
import kotlinx.coroutines.flow.Flow

interface StepRepository {
    suspend fun getTodaySteps(): Flow<StepData?>
    suspend fun saveStepData(stepData: StepData)
    suspend fun getStepHistory(): Flow<List<StepData>>
}
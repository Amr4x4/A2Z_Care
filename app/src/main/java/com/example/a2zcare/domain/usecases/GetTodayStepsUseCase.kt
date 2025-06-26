package com.example.a2zcare.domain.usecases


import com.example.a2zcare.domain.entities.StepData
import com.example.a2zcare.domain.repository.StepRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodayStepsUseCase @Inject constructor(
    private val stepRepository: StepRepository
) {
    suspend operator fun invoke(): Flow<StepData?> {
        return stepRepository.getTodaySteps()
    }
}
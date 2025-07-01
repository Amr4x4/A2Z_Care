package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.model.ActivityPredictionRequest
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

class PredictActivityUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<ActivityPredictionRequest, Result<String>>() {

    override suspend fun execute(parameters: ActivityPredictionRequest): Result<String> {
        return repository.predictActivity(parameters)
    }
}

class GetLatestActivityDataUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<String, Result<String>>() {

    override suspend fun execute(parameters: String): Result<String> {
        return repository.getLatestActivityData(parameters)
    }
}
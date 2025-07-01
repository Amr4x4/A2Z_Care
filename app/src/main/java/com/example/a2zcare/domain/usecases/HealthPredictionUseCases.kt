package com.example.a2zcare.domain.usecases

import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

class GetLatestBloodPressurePredictionUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<String, Result<String>>() {

    override suspend fun execute(parameters: String): Result<String> {
        return repository.getLatestBloodPressurePrediction(parameters)
    }
}

class SendBloodPressureAIUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<SendBloodPressureAIUseCase.Params, Result<String>>() {

    data class Params(val userId: String, val batchSize: Int)

    override suspend fun execute(parameters: Params): Result<String> {
        return repository.sendBloodPressureAI(parameters.userId, parameters.batchSize)
    }
}

class GetLatestHeartDiseasePredictionUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<String, Result<String>>() {

    override suspend fun execute(parameters: String): Result<String> {
        return repository.getLatestHeartDiseasePrediction(parameters)
    }
}

class SendHeartDiseaseAIUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<SendHeartDiseaseAIUseCase.Params, Result<String>>() {

    data class Params(val userId: String, val batchSize: Int)

    override suspend fun execute(parameters: Params): Result<String> {
        return repository.sendHeartDiseaseAI(parameters.userId, parameters.batchSize)
    }
}

class GetLatestHeartRateCalculationUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<String, Result<String>>() {

    override suspend fun execute(parameters: String): Result<String> {
        return repository.getLatestHeartRateCalculation(parameters)
    }
}

class SendHeartRateAIUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<SendHeartRateAIUseCase.Params, Result<String>>() {

    data class Params(val userId: String, val batchSize: Int)

    override suspend fun execute(parameters: Params): Result<String> {
        return repository.sendHeartRateAI(parameters.userId, parameters.batchSize)
    }
}

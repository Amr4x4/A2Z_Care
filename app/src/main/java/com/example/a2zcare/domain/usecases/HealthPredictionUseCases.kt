package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.remote.request.BloodPressureResult
import com.example.a2zcare.data.remote.request.HeartRateResult
import com.example.a2zcare.data.remote.response.HeartDiseasePredictionResponse
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

class GetLatestBloodPressurePredictionUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<String, Result<BloodPressureResult>>() {

    override suspend fun execute(parameters: String): Result<BloodPressureResult> {
        return repository.getLatestBloodPressurePrediction(parameters)
    }
}

class SendBloodPressureAIUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<SendBloodPressureAIUseCase.Params, Result<BloodPressureResult>>() {

    data class Params(val userId: String, val batchSize: Int)

    override suspend fun execute(parameters: Params): Result<BloodPressureResult> {
        return repository.sendBloodPressureAI(parameters.userId, parameters.batchSize)
    }
}

class GetLatestHeartDiseasePredictionUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<String, Result<HeartDiseasePredictionResponse>>() {

    override suspend fun execute(parameters: String): Result<HeartDiseasePredictionResponse> {
        return repository.getLatestHeartDiseasePrediction(parameters)
    }
}

class SendHeartDiseaseAIUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<SendHeartDiseaseAIUseCase.Params, Result<HeartDiseasePredictionResponse>>() {

    data class Params(val userId: String, val batchSize: Int)

    override suspend fun execute(parameters: Params): Result<HeartDiseasePredictionResponse> {
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
) : UseCase<SendHeartRateAIUseCase.Params, Result<HeartRateResult>>() {

    data class Params(val userId: String, val batchSize: Int)

    override suspend fun execute(parameters: Params): Result<HeartRateResult> {
        return repository.sendHeartRateAI(parameters.userId, parameters.batchSize)
    }
}

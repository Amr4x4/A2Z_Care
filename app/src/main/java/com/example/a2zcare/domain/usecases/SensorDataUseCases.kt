package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.model.SensorDataRequest
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

class ImportSensorDataUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<ImportSensorDataUseCase.Params, Result<Unit>>() {

    data class Params(val userId: String, val request: SensorDataRequest)

    override suspend fun execute(parameters: Params): Result<Unit> {
        return repository.importSensorData(parameters.userId, parameters.request)
    }
}
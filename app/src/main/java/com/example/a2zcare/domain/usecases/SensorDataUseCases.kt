package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.remote.response.SensorDataImportResponse
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import okhttp3.MultipartBody
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

class ImportSensorDataFileUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<ImportSensorDataFileUseCase.Params, Result<SensorDataImportResponse>>() {

    data class Params(val userId: String, val file: MultipartBody.Part)

    override suspend fun execute(parameters: Params): Result<SensorDataImportResponse> {
        return repository.importSensorDataFile(parameters.userId, parameters.file)
    }
}
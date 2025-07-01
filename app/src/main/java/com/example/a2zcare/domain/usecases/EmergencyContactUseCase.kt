package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.model.EmergencyContactRequest
import com.example.a2zcare.data.model.UserWithEmergencyContacts
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

class CreateEmergencyContactUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<CreateEmergencyContactUseCase.Params, Result<String>>() {

    data class Params(val userId: String, val request: EmergencyContactRequest)

    override suspend fun execute(parameters: Params): Result<String> {
        return repository.createEmergencyContact(parameters.userId, parameters.request)
    }
}

class GetEmergencyContactsUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<String, Result<UserWithEmergencyContacts>>() {

    override suspend fun execute(parameters: String): Result<UserWithEmergencyContacts> {
        return repository.getEmergencyContacts(parameters)
    }
}

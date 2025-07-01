package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.model.SendMessageRequest
import com.example.a2zcare.data.model.SendSMSRequest
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

class SendSMSUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<SendSMSRequest, Result<Unit>>() {

    override suspend fun execute(parameters: SendSMSRequest): Result<Unit> {
        return repository.sendSMS(parameters)
    }
}

class SendMessageUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<SendMessageRequest, Result<Unit>>() {

    override suspend fun execute(parameters: SendMessageRequest): Result<Unit> {
        return repository.sendMessage(parameters)
    }
}
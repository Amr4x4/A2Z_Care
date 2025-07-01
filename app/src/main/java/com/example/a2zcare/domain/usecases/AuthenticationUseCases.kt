package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.model.LoginRequest
import com.example.a2zcare.data.model.RegisterRequest
import com.example.a2zcare.data.model.ResetPasswordRequest
import com.example.a2zcare.data.model.UpdateUserRequest
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result


class RegisterUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<RegisterRequest, Result<Unit>>() {

    override suspend fun execute(parameters: RegisterRequest): Result<Unit> {
        return repository.register(parameters)
    }
}

class LoginUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<LoginRequest, Result<Unit>>() {

    override suspend fun execute(parameters: LoginRequest): Result<Unit> {
        return repository.login(parameters)
    }
}

class LogoutUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<Unit, Result<Unit>>() {

    override suspend fun execute(parameters: Unit): Result<Unit> {
        return repository.logout()
    }
}

class GetUserDataUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<String, Result<String>>() {

    override suspend fun execute(parameters: String): Result<String> {
        return repository.getUserData(parameters)
    }
}

class UpdateUserUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<UpdateUserUseCase.Params, Result<String>>() {

    data class Params(val id: String, val request: UpdateUserRequest)

    override suspend fun execute(parameters: Params): Result<String> {
        return repository.updateUser(parameters.id, parameters.request)
    }
}

class ResetPasswordUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<ResetPasswordRequest, Result<Unit>>() {

    override suspend fun execute(parameters: ResetPasswordRequest): Result<Unit> {
        return repository.resetPassword(parameters)
    }
}

class ForgotPasswordUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase<String, Result<Unit>>() {

    override suspend fun execute(parameters: String): Result<Unit> {
        return repository.forgotPassword(parameters)
    }
}

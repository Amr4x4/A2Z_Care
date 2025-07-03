package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.model.User
import com.example.a2zcare.data.remote.request.LoginRequest
import com.example.a2zcare.data.remote.request.RegisterRequest
import com.example.a2zcare.data.remote.request.ResetPasswordRequest
import com.example.a2zcare.data.remote.request.UpdateUserRequest
import com.example.a2zcare.data.remote.request.SendEmailRequest
import com.example.a2zcare.data.remote.response.LoginResponse
import com.example.a2zcare.data.remote.response.RegisterResponse
import com.example.a2zcare.data.remote.response.SendEmailResponse
import com.example.a2zcare.data.remote.response.UpdateUserResponse
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import javax.inject.Inject
import com.example.a2zcare.domain.model.Result

abstract class UseCase0<in P, R> {
    abstract suspend fun execute(parameters: P): R
    suspend operator fun invoke(parameters: P): R = execute(parameters)
}


class RegisterUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase0<RegisterRequest, Result<RegisterResponse>>() {

    override suspend fun execute(parameters: RegisterRequest): Result<RegisterResponse> {
        return repository.register(parameters)
    }
}

class LoginUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase0<LoginRequest, Result<LoginResponse>>() {

    override suspend fun execute(parameters: LoginRequest): Result<LoginResponse> {
        return repository.login(parameters)
    }
}

class GetUserDataUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase0<String, Result<User>>() {

    override suspend fun execute(parameters: String): Result<User> {
        return repository.getUserData(parameters)
    }
}

class UpdateUserUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase0<UpdateUserUseCase.Params, Result<UpdateUserResponse>>() {

    data class Params(val id: String, val request: UpdateUserRequest)

    override suspend fun execute(parameters: Params): Result<UpdateUserResponse> {
        return repository.updateUser(parameters.id, parameters.request)
    }
}

class ResetPasswordUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase0<ResetPasswordRequest, Result<Unit>>() {

    override suspend fun execute(parameters: ResetPasswordRequest): Result<Unit> {
        return repository.resetPassword(parameters)
    }
}

class ForgotPasswordUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase0<String, Result<Unit>>() {

    override suspend fun execute(parameters: String): Result<Unit> {
        return repository.forgotPassword(parameters)
    }
}

class SendEmailUseCase @Inject constructor(
    private val repository: HealthMonitoringRepository
) : UseCase0<SendEmailRequest, Result<SendEmailResponse>>() {

    override suspend fun execute(parameters: SendEmailRequest): Result<SendEmailResponse> {
        return repository.sendEmail(parameters)
    }
}
package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.network.response.LoginResultResponse
import com.example.a2zcare.domain.model.NetworkResult
import com.example.a2zcare.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): NetworkResult<LoginResultResponse> {

        if (email.isBlank()) {
            return NetworkResult.Error("Email cannot be empty")
        }

        if (password.isBlank()) {
            return NetworkResult.Error("Password cannot be empty")
        }

        return authRepository.login(email, password)
    }
}
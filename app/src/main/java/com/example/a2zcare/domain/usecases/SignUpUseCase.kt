package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.network.response.SignUpResultResponse
import com.example.a2zcare.domain.model.NetworkResult
import com.example.a2zcare.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        userName: String,
        email: String,
        password: String,
        role: Int = 0
    ): NetworkResult<SignUpResultResponse> {

        // Add any business logic validation here if needed
        if (userName.isBlank()) {
            return NetworkResult.Error("Username cannot be empty")
        }

        if (email.isBlank()) {
            return NetworkResult.Error("Email cannot be empty")
        }

        if (password.isBlank()) {
            return NetworkResult.Error("Password cannot be empty")
        }

        return authRepository.signUp(userName, email, password, role)
    }
}

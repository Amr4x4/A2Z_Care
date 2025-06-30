package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.network.response.ForgotPasswordResponse
import com.example.a2zcare.domain.model.NetworkResult
import com.example.a2zcare.domain.repository.AuthRepository
import javax.inject.Inject

class ForgetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): NetworkResult<ForgotPasswordResponse> {
        return authRepository.forgetPassword(email)
    }
}
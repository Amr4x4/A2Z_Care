package com.example.a2zcare.domain.usecases

import com.example.a2zcare.domain.entities.SignUpResult
import com.example.a2zcare.domain.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(userName: String, email: String, password: String): Result<SignUpResult> {
        return repository.signUp(userName, email, password)
    }
}
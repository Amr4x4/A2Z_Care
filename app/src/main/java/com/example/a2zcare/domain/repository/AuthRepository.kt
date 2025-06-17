package com.example.a2zcare.domain.repository

import com.example.a2zcare.domain.entities.SignUpResult

interface AuthRepository {
    suspend fun signUp(userName: String, email: String, password: String): Result<SignUpResult>
}
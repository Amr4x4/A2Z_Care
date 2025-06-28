package com.example.a2zcare.domain.repository

import com.example.a2zcare.data.network.response.SignUpResponse
import com.example.a2zcare.domain.model.NetworkResult

interface AuthRepository {
    suspend fun signUp(
        userName: String,
        email: String,
        password: String,
        role: Int = 0
    ): NetworkResult<SignUpResponse>
}
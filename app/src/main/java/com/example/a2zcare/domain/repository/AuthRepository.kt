package com.example.a2zcare.domain.repository

import com.example.a2zcare.data.network.response.LoginResultResponse
import com.example.a2zcare.data.network.response.SignUpResultResponse
import com.example.a2zcare.domain.model.NetworkResult

interface AuthRepository {
    suspend fun signUp(
        userName: String,
        email: String,
        password: String,
        role: Int = 0
    ): NetworkResult<SignUpResultResponse>

    suspend fun login(
        email: String,
        password: String
    ): NetworkResult<LoginResultResponse>
}
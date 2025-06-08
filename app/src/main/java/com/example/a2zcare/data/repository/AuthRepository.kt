package com.example.a2zcare.data.repository

import com.example.a2zcare.data.network.ApiService
import com.example.a2zcare.data.network.SignUpRequest
import com.example.a2zcare.data.network.SignUpResponse
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun signUp(userName: String, email: String, password: String): Response<SignUpResponse> {
        val request = SignUpRequest(
            userName = userName,
            email = email,
            password = password,
            role = 0
        )
        return apiService.signUp(request)
    }
}

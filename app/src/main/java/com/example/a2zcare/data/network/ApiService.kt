package com.example.a2zcare.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class SignUpRequest(
    val userName: String,
    val password: String,
    val email: String,
    val role: Int = 0
)

data class SignUpResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String? = null
)

interface ApiService {
    @POST("signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    // Other endpoints...
}

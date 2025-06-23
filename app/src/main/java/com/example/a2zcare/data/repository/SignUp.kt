package com.example.a2zcare.data.repository

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
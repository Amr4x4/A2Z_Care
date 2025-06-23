package com.example.a2zcare.data.repository

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String? = null
)
package com.example.a2zcare.data.model

data class UserResponse(
    val statusCode: Int,
    val isSuccess: Boolean,
    val errors: List<String>,
    val result: User?
)

data class User(
    val id: String,
    val userName: String,
    val email: String,
    val role: String
)

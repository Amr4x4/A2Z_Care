package com.example.a2zcare.data.model

data class RegisterRequest(
    val userName: String,
    val password: String,
    val email: String,
    val role: Int = 0
)

data class LoginRequest(
    val email: String,
    val password: String,
    val username: String
)

data class LogoutRequest(
    val userId: String,
    val token: String,
    val deviceId: String
)

data class UpdateUserRequest(
    val id: String,
    val firstName: String,
    val lastName: String,
    val name: String,
    val phoneNumber: String,
    val address: String,
    val age: Int,
    val dateOfBirth: String,
    val gender: String,
    val weightKg: Double,
    val height: Double,
    val healthGoals: String,
    val updatedAt: String
)

data class ResetPasswordRequest(
    val email: String,
    val currentPassword: String,
    val newPassword: String
)

data class ResetForgotPasswordRequest(
    val userId: String,
    val token: String,
    val newPassword: String
)

data class SendEmailRequest(
    val toEmail: String,
    val subject: String,
    val body: String,
    val attachments: List<String>?
)
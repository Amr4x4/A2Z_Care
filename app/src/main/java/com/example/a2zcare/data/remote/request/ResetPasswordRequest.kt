package com.example.a2zcare.data.remote.request

data class ResetPasswordRequest(
    val email: String,
    val currentPassword: String,
    val newPassword: String
)

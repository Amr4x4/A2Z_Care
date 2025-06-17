package com.example.a2zcare.domain.entities

data class SignUpResult(
    val success: Boolean,
    val message: String?,
    val token: String?
)
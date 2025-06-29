package com.example.a2zcare.domain.usecases

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
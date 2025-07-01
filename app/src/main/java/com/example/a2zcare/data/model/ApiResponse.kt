package com.example.a2zcare.data.model

data class ApiResponse<T>(
    val statusCode: Int,
    val isSuccess: Boolean,
    val errors: List<String>?,
    val result: T?
)
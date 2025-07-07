package com.example.a2zcare.data.remote.response

data class ApiResponseEmail<T>(
    val statusCode: Int,
    val isSuccess: Boolean,
    val errors: List<String>,
    val result: T?
)
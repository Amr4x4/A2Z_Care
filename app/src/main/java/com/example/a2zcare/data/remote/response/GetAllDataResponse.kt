package com.example.a2zcare.data.remote.response

data class BloodPressureData(
    val timestamp: String,
    val sbp: Double,
    val dbp: Double,
    val category: String
)

data class HeartDiseaseData(
    val diseases: String,
    val recordedAt: String
)

data class HeartRateData(
    val recordedAt: String,
    val heartRate: Int,
    val category: String
)
data class ApiResponse2<T>(
    val statusCode: Int,
    val isSuccess: Boolean,
    val errors: List<String>,
    val result: T
)

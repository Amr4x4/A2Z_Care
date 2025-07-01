package com.example.a2zcare.data.model

data class SensorDataRequest(
    val userId: String,
    val ppg: List<Double>,
    val abp: List<Double>,
    val ecg: List<Double>,
    val timestamp: String
)
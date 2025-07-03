package com.example.a2zcare.data.model

data class EmergencyAlertRequest(
    val userId: String,
    val emergencyContactIds: List<String>,
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val message: String
)
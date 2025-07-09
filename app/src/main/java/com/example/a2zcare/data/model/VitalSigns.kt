package com.example.a2zcare.data.model

data class VitalSigns2(
    val systolicBP: Int,
    val diastolicBP: Int,
    val heartRate: Int,
    val timestamp: Long = System.currentTimeMillis()
)
data class EmergencyContact2(
    val id: Int = 0,
    val name: String,
    val phoneNumber: String,
    val email: String? = null,
    val relation: String,
    val isPrimary: Boolean = false
)

data class EmergencyAlert(
    val phoneNumber: String,
    val body: String
)

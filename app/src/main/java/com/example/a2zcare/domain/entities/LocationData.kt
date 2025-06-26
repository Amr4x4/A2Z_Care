package com.example.a2zcare.domain.entities

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val address: String? = null
)
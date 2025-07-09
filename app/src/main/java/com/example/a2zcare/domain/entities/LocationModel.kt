package com.example.a2zcare.domain.entities

import com.example.a2zcare.data.remote.response.LocationData

data class LocationData2(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val address: String? = null // Optional, useful for display and messaging
)

data class LocationShare(
    val id: String,
    val fromUserId: String,
    val toUserId: String,
    val location: LocationData,
    val message: String? = null,
    val sharedAt: Long = System.currentTimeMillis()
)


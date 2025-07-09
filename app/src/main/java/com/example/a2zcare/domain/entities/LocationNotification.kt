package com.example.a2zcare.domain.entities

import com.example.a2zcare.data.remote.response.LocationData
import java.util.Date

data class LocationNotification(
    val id: String,
    val fromUserId: String,
    val fromUserName: String,
    val location: LocationData,
    val message: String,
    val timestamp: Date,
    val isRead: Boolean = false
)

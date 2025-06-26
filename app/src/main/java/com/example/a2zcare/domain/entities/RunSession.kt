package com.example.a2zcare.domain.entities

import java.util.Date

data class RunSession(
    val id: String,
    val startTime: Date,
    val endTime: Date?,
    val distance: Double,
    val steps: Int,
    val caloriesBurned: Double,
    val route: List<LocationData>,
    val isActive: Boolean = false
)
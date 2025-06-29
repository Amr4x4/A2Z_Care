package com.example.a2zcare.domain.entities

data class StepDataTracker(
    val id: String,
    val userId: String,
    val date: String,
    val steps: Int,
    val caloriesBurned: Int,
    val distanceKm: Float,
    val activeMinutes: Int,
    val lastUpdated: Long
)
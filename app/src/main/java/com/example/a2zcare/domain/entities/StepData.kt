package com.example.a2zcare.domain.entities

import java.util.Date

data class StepData(
    val date: Date,
    val steps: Int,
    val caloriesBurned: Double,
    val startLocation: LocationData?,
    val endLocation: LocationData?
)
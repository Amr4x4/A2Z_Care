package com.example.a2zcare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "step_data")
data class StepDataEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val date: String,
    val steps: Int,
    val caloriesBurned: Int,
    val distanceKm: Float,
    val activeMinutes: Int,
    val lastUpdated: Long
)
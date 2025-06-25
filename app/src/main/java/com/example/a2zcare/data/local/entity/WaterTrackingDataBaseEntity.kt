package com.example.a2zcare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_intake_entries")
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val timestamp: Long,
    val amount: Int,
    val glassType: String
)

@Entity(tableName = "daily_water_goals")
data class DailyWaterGoalEntity(
    @PrimaryKey
    val date: String,
    val id: Long = 0,
    val targetAmount: Int,
    val achievedAmount: Int
)

@Entity(tableName = "intervals")
data class IntervalEntity(
    @PrimaryKey
    val minute: Int,
    val selected: Boolean = false,
    val displayName: String = "${minute} min"
)

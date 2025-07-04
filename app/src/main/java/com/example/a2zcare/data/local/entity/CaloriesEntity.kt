package com.example.a2zcare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "calories_intake_entries")
data class CaloriesIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val timestamp: Long,
    val calories: Double,
    val foodName: String,
    val grams: Double,
    val mealType: String? = null // Added for meal type tracking
)

@Entity(tableName = "daily_calories_goals")
data class DailyCaloriesGoalEntity(
    @PrimaryKey
    val date: String,
    val id: Long = 0,
    val targetCalories: Double,
    val achievedCalories: Double
)

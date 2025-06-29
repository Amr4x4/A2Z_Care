package com.example.a2zcare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val age: Int,
    val height: Float,
    val weight: Float,
    val gender: String,
    val activityLevel: String,
    val calorieIntakeType: String,
    val dailyStepsTarget: Int,
    val dailyCaloriesBurnTarget: Int,
    val createdAt: Long
)
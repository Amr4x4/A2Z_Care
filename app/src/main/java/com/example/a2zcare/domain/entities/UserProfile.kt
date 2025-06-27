package com.example.a2zcare.domain.entities

data class UserProfile(
    val id: String,
    val age: Int,
    val height: Float, // in cm
    val weight: Float, // in kg
    val gender: Gender,
    val activityLevel: ActivityLevel,
    val calorieIntakeType: CalorieIntakeType,
    val dailyStepsTarget: Int,
    val dailyCaloriesBurnTarget: Int,
    val createdAt: Long
)

enum class Gender {
    MALE, FEMALE, OTHER
}

enum class ActivityLevel {
    SEDENTARY,
    LIGHTLY_ACTIVE,
    MODERATELY_ACTIVE,
    VERY_ACTIVE,
    EXTREMELY_ACTIVE
}

enum class CalorieIntakeType {
    MAINTENANCE,
    WEIGHT_LOSS,
    WEIGHT_GAIN,
    MUSCLE_BUILDING
}

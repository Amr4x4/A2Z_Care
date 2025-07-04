package com.example.a2zcare.domain.entities

data class DailyCaloriesGoal(
    val id: Long = 0,
    val date: String,
    val targetCalories: Double,
    val achievedCalories: Double,
    val goalPercentage: Float
)

data class CaloriesIntakeEntry(
    val id: Long = 0,
    val timestamp: Long,
    val calories: Double,
    val foodName: String,
    val portion: Double, // was grams, now portion for consistency
    val mealType: String? = null // Added for meal type tracking
)

data class FoodItem(
    val id: Int,
    val name: String,
    val caloriesPer100g: Double,
    val category: FoodCategory
)

data class FoodCategory(
    val id: Int,
    val name: String,
    val icon: String
)



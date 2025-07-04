package com.example.a2zcare.domain.usecases

import com.example.a2zcare.domain.entities.ActivityLevel
import com.example.a2zcare.domain.entities.CalorieIntakeType
import com.example.a2zcare.domain.entities.Gender

class CalculateTargetsUseCase {

    fun calculateDailyStepsTarget(
        age: Int,
        gender: Gender,
        activityLevel: ActivityLevel,
        weight: Float,
        height: Float
    ): Int {
        val baseSteps = when (activityLevel) {
            ActivityLevel.SEDENTARY -> 5000
            ActivityLevel.LIGHTLY_ACTIVE -> 7500
            ActivityLevel.MODERATELY_ACTIVE -> 10000
            ActivityLevel.VERY_ACTIVE -> 12500
            ActivityLevel.EXTREMELY_ACTIVE -> 15000
        }

        // Adjust based on age
        val ageMultiplier = when {
            age < 18 -> 1.2f
            age in 18..30 -> 1.1f
            age in 31..50 -> 1.0f
            age in 51..65 -> 0.9f
            else -> 0.8f
        }

        // Adjust based on gender
        val genderMultiplier = when (gender) {
            Gender.MALE -> 1.1f
            Gender.FEMALE -> 1.0f
            Gender.OTHER -> 1.05f
        }

        return (baseSteps * ageMultiplier * genderMultiplier).toInt()
    }

    fun calculateDailyCaloriesGainTarget(
        age: Int,
        gender: Gender,
        weight: Float,
        height: Float,
        activityLevel: ActivityLevel,
        calorieIntakeType: CalorieIntakeType
    ): Int {
        // Calculate BMR (Basal Metabolic Rate)
        val bmr = if (gender == Gender.MALE) {
            88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age)
        } else {
            447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age)
        }

        // Apply activity multiplier
        val activityMultiplier = when (activityLevel) {
            ActivityLevel.SEDENTARY -> 1.2
            ActivityLevel.LIGHTLY_ACTIVE -> 1.375
            ActivityLevel.MODERATELY_ACTIVE -> 1.55
            ActivityLevel.VERY_ACTIVE -> 1.725
            ActivityLevel.EXTREMELY_ACTIVE -> 1.9
        }

        val totalDailyCalories = bmr * activityMultiplier

        // Adjust based on calorie intake type (goal)
        val calorieAdjustment = when (calorieIntakeType) {
            CalorieIntakeType.WEIGHT_LOSS -> totalDailyCalories * 0.8
            CalorieIntakeType.WEIGHT_GAIN -> totalDailyCalories * 1.1
            CalorieIntakeType.MUSCLE_BUILDING -> totalDailyCalories * 1.15
            CalorieIntakeType.MAINTENANCE -> totalDailyCalories
        }

        // Return calories to gain (target intake)
        return calorieAdjustment.toInt()
    }
}
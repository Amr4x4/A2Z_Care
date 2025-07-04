package com.example.a2zcare.data.mapper

import com.example.a2zcare.data.local.entity.CaloriesIntakeEntity
import com.example.a2zcare.data.local.entity.DailyCaloriesGoalEntity
import com.example.a2zcare.domain.entities.CaloriesIntakeEntry
import com.example.a2zcare.domain.entities.DailyCaloriesGoal

fun DailyCaloriesGoalEntity.toDomain(): DailyCaloriesGoal {
    val percentage = if (targetCalories > 0) {
        (achievedCalories / targetCalories * 100.0).coerceAtMost(100.0)
    } else 0.0

    return DailyCaloriesGoal(
        id = id,
        date = date,
        targetCalories = targetCalories,
        achievedCalories = achievedCalories,
        goalPercentage = percentage.toFloat()
    )
}

fun DailyCaloriesGoal.toEntity(): DailyCaloriesGoalEntity {
    return DailyCaloriesGoalEntity(
        id = id,
        date = date,
        targetCalories = targetCalories,
        achievedCalories = achievedCalories
    )
}

fun CaloriesIntakeEntity.toDomain(): CaloriesIntakeEntry {
    return CaloriesIntakeEntry(
        id = id,
        timestamp = timestamp,
        calories = calories,
        foodName = foodName,
        portion = grams, // map DB grams to domain portion
        mealType = mealType // map mealType
    )
}

fun CaloriesIntakeEntry.toEntity(date: String): CaloriesIntakeEntity {
    return CaloriesIntakeEntity(
        id = id,
        date = date,
        timestamp = timestamp,
        calories = calories,
        foodName = foodName,
        grams = portion, // map domain portion to DB grams
        mealType = mealType // map mealType
    )
}
package com.example.a2zcare.data.mapper

import com.example.a2zcare.data.local.entity.UserProfileEntity
import com.example.a2zcare.domain.entities.ActivityLevel
import com.example.a2zcare.domain.entities.CalorieIntakeType
import com.example.a2zcare.domain.entities.Gender
import com.example.a2zcare.domain.entities.UserProfile

fun UserProfile.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = this.id,
        age = this.age,
        height = this.height,
        weight = this.weight,
        gender = this.gender.name,
        activityLevel = this.activityLevel.name,
        calorieIntakeType = this.calorieIntakeType.name,
        dailyStepsTarget = this.dailyStepsTarget,
        dailyCaloriesBurnTarget = this.dailyCaloriesBurnTarget,
        createdAt = this.createdAt
    )
}

fun UserProfileEntity.toDomain(): UserProfile {
    return UserProfile(
        id = this.id,
        age = this.age,
        height = this.height,
        weight = this.weight,
        gender = Gender.valueOf(this.gender),
        activityLevel = ActivityLevel.valueOf(this.activityLevel),
        calorieIntakeType = CalorieIntakeType.valueOf(this.calorieIntakeType),
        dailyStepsTarget = this.dailyStepsTarget,
        dailyCaloriesBurnTarget = this.dailyCaloriesBurnTarget,
        createdAt = this.createdAt
    )
}

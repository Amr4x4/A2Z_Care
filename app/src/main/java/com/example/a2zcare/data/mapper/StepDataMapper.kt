package com.example.a2zcare.data.mapper

import com.example.a2zcare.data.local.entity.StepDataEntity
import com.example.a2zcare.domain.entities.StepDataTracker

fun StepDataTracker.toEntity(): StepDataEntity {
    return StepDataEntity(
        id = this.id,
        userId = this.userId,
        date = this.date,
        steps = this.steps,
        caloriesBurned = this.caloriesBurned,
        distanceKm = this.distanceKm,
        activeMinutes = this.activeMinutes,
        lastUpdated = this.lastUpdated
    )
}

fun StepDataEntity.toDomain(): StepDataTracker {
    return StepDataTracker(
        id = this.id,
        userId = this.userId,
        date = this.date,
        steps = this.steps,
        caloriesBurned = this.caloriesBurned,
        distanceKm = this.distanceKm,
        activeMinutes = this.activeMinutes,
        lastUpdated = this.lastUpdated
    )
}

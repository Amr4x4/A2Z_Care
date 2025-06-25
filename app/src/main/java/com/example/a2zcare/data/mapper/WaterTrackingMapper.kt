package com.example.a2zcare.data.mapper


import com.example.a2zcare.data.local.entity.DailyWaterGoalEntity
import com.example.a2zcare.data.local.entity.IntervalEntity
import com.example.a2zcare.data.local.entity.WaterIntakeEntity
import com.example.a2zcare.domain.entities.DailyWaterGoal
import com.example.a2zcare.domain.entities.GlassSize
import com.example.a2zcare.domain.entities.Interval
import com.example.a2zcare.domain.entities.WaterIntakeEntry

fun DailyWaterGoalEntity.toDomain(): DailyWaterGoal {
    val percentage = if (targetAmount > 0) {
        (achievedAmount.toFloat() / targetAmount.toFloat() * 100f).coerceAtMost(100f)
    } else 0f

    return DailyWaterGoal(
        id = id,
        date = date,
        targetAmount = targetAmount,
        achievedAmount = achievedAmount,
        goalPercentage = percentage
    )
}

fun DailyWaterGoal.toEntity(): DailyWaterGoalEntity {
    return DailyWaterGoalEntity(
        id = id,
        date = date,
        targetAmount = targetAmount,
        achievedAmount = achievedAmount
    )
}

fun IntervalEntity.toDomain(): Interval {
    return Interval(
        minute = minute,
        selected = selected,
        displayName = displayName
    )
}

fun Interval.toEntity(): IntervalEntity {
    return IntervalEntity(
        minute = minute,
        selected = selected,
        displayName = displayName
    )
}

fun WaterIntakeEntity.toDomain(): WaterIntakeEntry {
    return WaterIntakeEntry(
        id = id,
        timestamp = timestamp,
        amount = amount,
        glassSize = GlassSize.values().find { it.name == glassType } ?: GlassSize.MEDIUM
    )
}

fun WaterIntakeEntry.toEntity(date: String): WaterIntakeEntity {
    return WaterIntakeEntity(
        id = id,
        date = date,
        timestamp = timestamp,
        amount = amount,
        glassType = glassSize.name
    )
}

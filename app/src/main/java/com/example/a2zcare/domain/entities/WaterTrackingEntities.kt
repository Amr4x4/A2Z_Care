package com.example.a2zcare.domain.entities

data class DailyWaterGoal(
    val id: Long = 0,
    val date: String,
    val targetAmount: Int,
    val achievedAmount: Int,
    val goalPercentage: Float
)

data class WaterIntakeEntry(
    val id: Long = 0,
    val timestamp: Long,
    val amount: Int,
    val glassSize: GlassSize
)

enum class GlassSize(val displayName: String, val ml: Int) {
    SMALL("Small (150ml)", 150),
    MEDIUM("Medium (250ml)", 250),
    LARGE("Large (350ml)", 350),
    BOTTLE("Bottle (500ml)", 500)
}

data class Interval(
    val minute: Int,
    val selected: Boolean = false,
    val displayName: String = "$minute min"
)
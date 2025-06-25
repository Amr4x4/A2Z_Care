package com.example.a2zcare.domain.repository

import com.example.a2zcare.domain.entities.DailyWaterGoal
import com.example.a2zcare.domain.entities.Interval
import com.example.a2zcare.domain.entities.WaterIntakeEntry

interface WaterTrackingRepository {
    suspend fun addWaterIntake(entry: WaterIntakeEntry)
    suspend fun getDailyIntake(date: String): List<WaterIntakeEntry>
    suspend fun updateDailyGoal(goal: DailyWaterGoal)
    suspend fun getWeeklyData(startDate: String): List<DailyWaterGoal>
    suspend fun getAllIntervals(): List<Interval>
    suspend fun updateInterval(interval: Interval)
    suspend fun getDailyGoal(date: String): DailyWaterGoal
}
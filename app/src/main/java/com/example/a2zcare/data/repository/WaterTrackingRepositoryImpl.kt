package com.example.a2zcare.data.repository


import com.example.a2zcare.data.local.dao.IntervalDao
import com.example.a2zcare.data.local.dao.WaterTrackingDao
import com.example.a2zcare.data.local.entity.DailyWaterGoalEntity
import com.example.a2zcare.data.local.entity.IntervalEntity
import com.example.a2zcare.data.mapper.toDomain
import com.example.a2zcare.data.mapper.toEntity
import com.example.a2zcare.domain.entities.DailyWaterGoal
import com.example.a2zcare.domain.entities.Interval
import com.example.a2zcare.domain.entities.WaterIntakeEntry
import com.example.a2zcare.domain.repository.WaterTrackingRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterTrackingRepositoryImpl @Inject constructor(
    private val waterDao: WaterTrackingDao,
    private val intervalDao: IntervalDao
) : WaterTrackingRepository {

    override suspend fun addWaterIntake(entry: WaterIntakeEntry) {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(entry.timestamp))
        waterDao.insertWaterIntake(entry.toEntity(date))
        updateDailyProgress(date)
    }

    override suspend fun getDailyIntake(date: String): List<WaterIntakeEntry> {
        return waterDao.getDailyIntakes(date).map { it.toDomain() }
    }

    override suspend fun updateDailyGoal(goal: DailyWaterGoal) {
        waterDao.insertOrUpdateGoal(goal.toEntity())
    }

    override suspend fun getWeeklyData(startDate: String): List<DailyWaterGoal> {
        // Implementation for weekly data - simplified for now
        return emptyList()
    }

    override suspend fun getAllIntervals(): List<Interval> {
        val intervals = intervalDao.getAllIntervals()
        if (intervals.isEmpty()) {
            // Initialize default intervals
            val defaultIntervals = listOf(
                IntervalEntity(15, false, "15 min"),
                IntervalEntity(30, true, "30 min"), // Default selected
                IntervalEntity(60, false, "1 hour"),
                IntervalEntity(120, false, "2 hours")
            )
            intervalDao.insertAll(defaultIntervals)
            return defaultIntervals.map { it.toDomain() }
        }
        return intervals.map { it.toDomain() }
    }

    override suspend fun updateInterval(interval: Interval) {
        intervalDao.deselectAllIntervals()
        intervalDao.updateInterval(interval.copy(selected = true).toEntity())
    }

    override suspend fun getDailyGoal(date: String): DailyWaterGoal {
        val existingGoal = waterDao.getDailyGoal(date)
        val totalIntake = waterDao.getTotalIntakeForDate(date) ?: 0

        return if (existingGoal != null) {
            existingGoal.copy(achievedAmount = totalIntake).toDomain()
        } else {
            val defaultGoal = DailyWaterGoalEntity(
                date = date,
                targetAmount = 2000, // Default 2L
                achievedAmount = totalIntake
            )
            waterDao.insertOrUpdateGoal(defaultGoal)
            defaultGoal.toDomain()
        }
    }

    private suspend fun updateDailyProgress(date: String) {
        val totalIntake = waterDao.getTotalIntakeForDate(date) ?: 0
        val existingGoal = waterDao.getDailyGoal(date)

        val updatedGoal = if (existingGoal != null) {
            existingGoal.copy(achievedAmount = totalIntake)
        } else {
            DailyWaterGoalEntity(
                date = date,
                targetAmount = 2000,
                achievedAmount = totalIntake
            )
        }
        waterDao.insertOrUpdateGoal(updatedGoal)
    }
}

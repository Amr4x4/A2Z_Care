
package com.example.a2zcare.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.a2zcare.data.local.entity.CaloriesIntakeEntity
import com.example.a2zcare.data.local.entity.DailyCaloriesGoalEntity

@Dao
interface CaloriesTrackingDao {
    @Query("SELECT * FROM calories_intake_entries WHERE date = :date ORDER BY timestamp DESC")
    suspend fun getDailyIntakes(date: String): List<CaloriesIntakeEntity>

    @Insert
    suspend fun insertCaloriesIntake(entry: CaloriesIntakeEntity)

    @Query("SELECT * FROM daily_calories_goals WHERE date = :date")
    suspend fun getDailyGoal(date: String): DailyCaloriesGoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateGoal(goal: DailyCaloriesGoalEntity)

    @Query("SELECT SUM(calories) FROM calories_intake_entries WHERE date = :date")
    suspend fun getTotalCaloriesForDate(date: String): Double?

    @Query("SELECT * FROM calories_intake_entries WHERE date >= :startDate AND date <= :endDate ORDER BY date")
    suspend fun getWeeklyIntakes(startDate: String, endDate: String): List<CaloriesIntakeEntity>

    @Query("DELETE FROM calories_intake_entries WHERE id = :id")
    suspend fun deleteIntakeEntry(id: Long)
}
package com.example.a2zcare.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.a2zcare.data.local.entity.DailyWaterGoalEntity
import com.example.a2zcare.data.local.entity.IntervalEntity
import com.example.a2zcare.data.local.entity.WaterIntakeEntity

@Dao
interface WaterTrackingDao {
    @Query("SELECT * FROM water_intake_entries WHERE date = :date ORDER BY timestamp DESC")
    suspend fun getDailyIntakes(date: String): List<WaterIntakeEntity>

    @Insert
    suspend fun insertWaterIntake(entry: WaterIntakeEntity)

    @Query("SELECT * FROM daily_water_goals WHERE date = :date")
    suspend fun getDailyGoal(date: String): DailyWaterGoalEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateGoal(goal: DailyWaterGoalEntity)

    @Query("SELECT SUM(amount) FROM water_intake_entries WHERE date = :date")
    suspend fun getTotalIntakeForDate(date: String): Int?

    @Query("SELECT * FROM water_intake_entries WHERE date >= :startDate AND date <= :endDate ORDER BY date")
    suspend fun getWeeklyIntakes(startDate: String, endDate: String): List<WaterIntakeEntity>
}

@Dao
interface IntervalDao {
    @Query("SELECT * FROM intervals ORDER BY minute")
    suspend fun getAllIntervals(): List<IntervalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterval(interval: IntervalEntity)

    @Update
    suspend fun updateInterval(interval: IntervalEntity)

    @Query("UPDATE intervals SET selected = 0")
    suspend fun deselectAllIntervals()

    @Query("SELECT * FROM intervals WHERE selected = 1 LIMIT 1")
    suspend fun getSelectedInterval(): IntervalEntity?

    @Query("DELETE FROM intervals")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(intervals: List<IntervalEntity>)
}

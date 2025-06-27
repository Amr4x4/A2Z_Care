package com.example.a2zcare.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.a2zcare.data.local.entity.StepDataEntity

@Dao
interface StepCounterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(stepData: StepDataEntity)

    @Query("SELECT * FROM step_data WHERE date = :date")
    suspend fun getStepDataByDate(date: String): StepDataEntity?

    @Query("SELECT * FROM step_data WHERE date BETWEEN :startDate AND :endDate ORDER BY date")
    suspend fun getStepDataRange(startDate: String, endDate: String): List<StepDataEntity>

    @Query("UPDATE step_data SET steps = :steps, caloriesBurned = :caloriesBurned WHERE date = :date")
    suspend fun updateStepCount(date: String, steps: Int, caloriesBurned: Int)

    @Query("SELECT * FROM step_data ORDER BY date DESC LIMIT 7")
    suspend fun getLastSevenDays(): List<StepDataEntity>
}

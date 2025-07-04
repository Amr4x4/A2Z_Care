package com.example.a2zcare.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a2zcare.data.local.dao.CaloriesTrackingDao
import com.example.a2zcare.data.local.entity.CaloriesIntakeEntity
import com.example.a2zcare.data.local.entity.DailyCaloriesGoalEntity

@Database(
    entities = [
        CaloriesIntakeEntity::class,
        DailyCaloriesGoalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CaloriesTrackingDatabase : RoomDatabase() {
    abstract fun caloriesTrackingDao(): CaloriesTrackingDao

    companion object {
        const val DATABASE_NAME = "calories_tracking_database"
    }
}
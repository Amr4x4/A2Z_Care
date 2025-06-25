package com.example.a2zcare.data.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a2zcare.data.local.dao.IntervalDao
import com.example.a2zcare.data.local.dao.WaterTrackingDao
import com.example.a2zcare.data.local.entity.DailyWaterGoalEntity
import com.example.a2zcare.data.local.entity.IntervalEntity
import com.example.a2zcare.data.local.entity.WaterIntakeEntity

@Database(
    entities = [
        WaterIntakeEntity::class,
        DailyWaterGoalEntity::class,
        IntervalEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WaterTrackingDatabase : RoomDatabase() {
    abstract fun waterTrackingDao(): WaterTrackingDao
    abstract fun intervalDao(): IntervalDao

    companion object {
        const val DATABASE_NAME = "water_tracking_database"
    }
}

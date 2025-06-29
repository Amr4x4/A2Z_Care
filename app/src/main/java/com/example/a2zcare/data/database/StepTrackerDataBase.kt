package com.example.a2zcare.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.a2zcare.data.local.Converters
import com.example.a2zcare.data.local.dao.StepCounterDao
import com.example.a2zcare.data.local.dao.UserProfileDao
import com.example.a2zcare.data.local.entity.StepDataEntity
import com.example.a2zcare.data.local.entity.UserProfileEntity

@Database(
    entities = [UserProfileEntity::class, StepDataEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class StepTrackerDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun stepCounterDao(): StepCounterDao

    companion object {
        @Volatile
        private var INSTANCE: StepTrackerDatabase? = null

        fun getDatabase(context: Context): StepTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StepTrackerDatabase::class.java,
                    "step_tracker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.example.a2zcare.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.a2zcare.data.local.dao.EmergencyDao
import com.example.a2zcare.data.local.entity.EmergencyContactEntity

@Database(
    entities = [EmergencyContactEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun emergencyDao(): EmergencyDao
}

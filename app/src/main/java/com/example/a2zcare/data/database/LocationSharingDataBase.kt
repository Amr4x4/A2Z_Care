package com.example.a2zcare.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.a2zcare.data.local.dao.EmergencyUserDao
import com.example.a2zcare.data.local.dao.SavedUserDao
import com.example.a2zcare.data.local.entity.EmergencyUserEntity
import com.example.a2zcare.data.local.entity.SavedUserEntity


@Database(
    entities = [SavedUserEntity::class, EmergencyUserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class LocationSharingDatabase : RoomDatabase() {
    abstract fun savedUserDao(): SavedUserDao
    abstract fun emergencyUserDao(): EmergencyUserDao

    companion object {
        @Volatile
        private var INSTANCE: LocationSharingDatabase? = null

        fun getDatabase(context: Context): LocationSharingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationSharingDatabase::class.java,
                    "location_sharing_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
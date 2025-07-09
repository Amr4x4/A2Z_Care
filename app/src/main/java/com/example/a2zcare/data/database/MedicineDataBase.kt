package com.example.a2zcare.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.a2zcare.data.local.Converters
import com.example.a2zcare.data.local.dao.MedicineDao
import com.example.a2zcare.data.local.dao.MedicineHistoryDao
import com.example.a2zcare.data.local.dao.MedicineScheduleDao
import com.example.a2zcare.data.local.entity.Medicine
import com.example.a2zcare.data.local.entity.MedicineHistory
import com.example.a2zcare.data.local.entity.MedicineSchedule

@Database(
    entities = [Medicine::class, MedicineSchedule::class, MedicineHistory::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MedicineDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun scheduleDao(): MedicineScheduleDao
    abstract fun historyDao(): MedicineHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: MedicineDatabase? = null

        fun getDatabase(context: Context): MedicineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedicineDatabase::class.java,
                    "medicine_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

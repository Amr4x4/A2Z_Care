package com.example.a2zcare.di

import android.content.Context
import com.example.a2zcare.data.database.MedicineDatabase
import com.example.a2zcare.data.local.dao.MedicineDao
import com.example.a2zcare.data.local.dao.MedicineHistoryDao
import com.example.a2zcare.data.local.dao.MedicineScheduleDao
import com.example.a2zcare.domain.repository.MedicineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MedicineModule {

    @Provides
    @Singleton
    fun provideMedicineDatabase(
        @ApplicationContext context: Context
    ): MedicineDatabase {
        return MedicineDatabase.getDatabase(context)
    }

    @Provides
    fun provideMedicineDao(database: MedicineDatabase): MedicineDao {
        return database.medicineDao()
    }

    @Provides
    fun provideScheduleDao(database: MedicineDatabase): MedicineScheduleDao {
        return database.scheduleDao()
    }

    @Provides
    fun provideHistoryDao(database: MedicineDatabase): MedicineHistoryDao {
        return database.historyDao()
    }

    @Provides
    @Singleton
    fun provideMedicineRepository(
        medicineDao: MedicineDao,
        scheduleDao: MedicineScheduleDao,
        historyDao: MedicineHistoryDao
    ): MedicineRepository {
        return MedicineRepository(medicineDao, scheduleDao, historyDao)
    }
}

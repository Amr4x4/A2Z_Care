package com.example.a2zcare.di

import android.content.Context
import androidx.room.Room
import com.example.a2zcare.data.database.WaterTrackingDatabase
import com.example.a2zcare.data.local.dao.IntervalDao
import com.example.a2zcare.data.local.dao.WaterTrackingDao
import com.example.a2zcare.data.repository.WaterTrackingRepositoryImpl
import com.example.a2zcare.domain.repository.WaterTrackingRepository
import com.example.a2zcare.notifications.water_tracking_notify.NotificationManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindWaterTrackingRepository(
        waterTrackingRepositoryImpl: WaterTrackingRepositoryImpl
    ): WaterTrackingRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWaterTrackingDatabase(
        @ApplicationContext context: Context
    ): WaterTrackingDatabase {
        return Room.databaseBuilder(
            context,
            WaterTrackingDatabase::class.java,
            WaterTrackingDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideWaterTrackingDao(database: WaterTrackingDatabase): WaterTrackingDao {
        return database.waterTrackingDao()
    }

    @Provides
    fun provideIntervalDao(database: WaterTrackingDatabase): IntervalDao {
        return database.intervalDao()
    }

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return NotificationManager(context)
    }
}
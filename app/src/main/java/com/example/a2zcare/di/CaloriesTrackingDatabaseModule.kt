package com.example.a2zcare.di

import android.content.Context
import androidx.room.Room
import com.example.a2zcare.data.database.CaloriesTrackingDatabase
import com.example.a2zcare.data.local.dao.CaloriesTrackingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CaloriesTrackingDatabaseModule {

    @Provides
    @Singleton
    fun provideCaloriesTrackingDatabase(
        @ApplicationContext context: Context
    ): CaloriesTrackingDatabase {
        return Room.databaseBuilder(
            context,
            CaloriesTrackingDatabase::class.java,
            CaloriesTrackingDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideCaloriesTrackingDao(database: CaloriesTrackingDatabase): CaloriesTrackingDao {
        return database.caloriesTrackingDao()
    }
}
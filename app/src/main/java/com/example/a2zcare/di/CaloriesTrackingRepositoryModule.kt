package com.example.a2zcare.di

import com.example.a2zcare.data.repository.CaloriesTrackingRepositoryImpl
import com.example.a2zcare.domain.repository.CaloriesTrackingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CaloriesTrackingRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCaloriesTrackingRepository(
        caloriesTrackingRepositoryImpl: CaloriesTrackingRepositoryImpl
    ): CaloriesTrackingRepository
}
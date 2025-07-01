package com.example.a2zcare.di

import com.example.a2zcare.data.repository.HealthMonitoringRepositoryImpl
import com.example.a2zcare.data.repository.WaterTrackingRepositoryImpl
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import com.example.a2zcare.domain.repository.WaterTrackingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindWaterTrackingRepository(
        waterTrackingRepositoryImpl: WaterTrackingRepositoryImpl
    ): WaterTrackingRepository

    @Binds
    abstract fun bindHealthMonitoringRepository(
        healthMonitoringRepositoryImpl: HealthMonitoringRepositoryImpl
    ): HealthMonitoringRepository

}


package com.example.a2zcare.di

import com.example.a2zcare.data.repository.LocationRepositoryImpl
import com.example.a2zcare.data.repository.NotificationRepositoryImpl
import com.example.a2zcare.data.repository.RunRepositoryImpl
import com.example.a2zcare.data.repository.StepRepositoryImpl
import com.example.a2zcare.data.repository.StepTrackerRepositoryImpl
import com.example.a2zcare.domain.repository.LocationRepository
import com.example.a2zcare.domain.repository.NotificationRepository
import com.example.a2zcare.domain.repository.RunRepository
import com.example.a2zcare.domain.repository.StepRepository
import com.example.a2zcare.domain.repository.StepTrackerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule2 {
    @Binds
    @Singleton
    abstract fun bindStepRepository(
        stepRepositoryImpl: StepRepositoryImpl
    ): StepRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindRunRepository(
        runRepositoryImpl: RunRepositoryImpl
    ): RunRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindStepTrackerRepository(
        stepTrackerRepositoryImpl: StepTrackerRepositoryImpl
    ): StepTrackerRepository
}

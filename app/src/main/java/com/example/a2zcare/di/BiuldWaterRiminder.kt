package com.example.a2zcare.di

import com.example.a2zcare.data.repository.AuthRepositoryImpl
import com.example.a2zcare.data.repository.WaterTrackingRepositoryImpl
import com.example.a2zcare.domain.repository.AuthRepository
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
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

}


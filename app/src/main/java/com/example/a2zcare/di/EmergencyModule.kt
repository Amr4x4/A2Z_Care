package com.example.a2zcare.di

import android.content.Context
import com.example.a2zcare.domain.usecases.LocationService
import com.example.a2zcare.domain.usecases.EmergencyCallService
import com.example.a2zcare.domain.usecases.SmsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmergencyModule {


    @Provides
    @Singleton
    fun provideCallService(@ApplicationContext context: Context): EmergencyCallService {
        return EmergencyCallService(context)
    }

    @Provides
    @Singleton
    fun provideLocationService(@ApplicationContext context: Context): LocationService {
        return LocationService(context)
    }


    @Provides
    @Singleton
    fun provideSmsService(context: Context): SmsService {
        return SmsService(context)
    }
}

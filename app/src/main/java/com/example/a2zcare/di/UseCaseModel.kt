package com.example.a2zcare.di

import com.example.a2zcare.domain.usecases.LocationService
import com.example.a2zcare.domain.repository.EmergencyRepository
import com.example.a2zcare.domain.repository.StepTrackerRepository
import com.example.a2zcare.domain.usecases.CalculateTargetsUseCase
import com.example.a2zcare.domain.usecases.StepTrackingUseCase
import com.example.a2zcare.domain.usecases.TriggerEmergencyUseCase
import com.example.a2zcare.domain.usecases.ValidateEmailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideTriggerEmergencyUseCase(
        emergencyRepository: EmergencyRepository,
        locationService: LocationService
    ): TriggerEmergencyUseCase {
        return TriggerEmergencyUseCase(emergencyRepository, locationService)
    }
    @Provides
    @Singleton
    fun provideCalculateTargetsUseCase(): CalculateTargetsUseCase {
        return CalculateTargetsUseCase()
    }

    @Provides
    @Singleton
    fun provideStepTrackingUseCase(
        repository: StepTrackerRepository,
        calculateTargetsUseCase: CalculateTargetsUseCase
    ): StepTrackingUseCase {
        return StepTrackingUseCase(repository)
    }

    @Provides
    fun provideValidateEmailUseCase(): ValidateEmailUseCase {
        return ValidateEmailUseCase()
    }

}
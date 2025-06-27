package com.example.a2zcare.domain.repository

import com.example.a2zcare.domain.entities.StepDataTracker
import com.example.a2zcare.domain.entities.UserProfile

interface StepTrackerRepository {
    suspend fun saveUserProfile(userProfile: UserProfile)
    suspend fun getUserProfile(): UserProfile?
    suspend fun saveStepData(stepData: StepDataTracker)
    suspend fun getStepDataByDate(date: String): StepDataTracker?
    suspend fun getStepDataRange(startDate: String, endDate: String): List<StepDataTracker>
    suspend fun updateStepCount(date: String, steps: Int, caloriesBurned: Int)
    suspend fun getLastSevenDays(): List<StepDataTracker>
}
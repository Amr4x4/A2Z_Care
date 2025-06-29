package com.example.a2zcare.data.repository

import com.example.a2zcare.data.local.dao.StepCounterDao
import com.example.a2zcare.data.local.dao.UserProfileDao
import com.example.a2zcare.data.mapper.toDomain
import com.example.a2zcare.data.mapper.toEntity
import com.example.a2zcare.domain.entities.StepDataTracker
import com.example.a2zcare.domain.entities.UserProfile
import com.example.a2zcare.domain.repository.StepTrackerRepository
import javax.inject.Inject

class StepTrackerRepositoryImpl @Inject constructor(
    private val stepCounterDao: StepCounterDao,
    private val userProfileDao: UserProfileDao
) : StepTrackerRepository {

    override suspend fun saveUserProfile(userProfile: UserProfile) {
        userProfileDao.insertOrUpdate(userProfile.toEntity())
    }

    override suspend fun getUserProfile(): UserProfile? {
        return userProfileDao.getUserProfile()?.toDomain()
    }

    override suspend fun saveStepData(stepData: StepDataTracker) {
        stepCounterDao.insertOrUpdate(stepData.toEntity())
    }

    override suspend fun getStepDataByDate(date: String): StepDataTracker? {
        return stepCounterDao.getStepDataByDate(date)?.toDomain()
    }

    override suspend fun getStepDataRange(startDate: String, endDate: String): List<StepDataTracker> {
        return stepCounterDao.getStepDataRange(startDate, endDate).map { it.toDomain() }
    }

    override suspend fun updateStepCount(date: String, steps: Int, caloriesBurned: Int) {
        stepCounterDao.updateStepCount(date, steps, caloriesBurned)
    }

    override suspend fun getLastSevenDays(): List<StepDataTracker> {
        return stepCounterDao.getLastSevenDays().map { it.toDomain() }
    }
}
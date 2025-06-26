package com.example.a2zcare.domain.repository

import com.example.a2zcare.domain.entities.LocationData
import com.example.a2zcare.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getCurrentLocation(): Flow<LocationData?>
    suspend fun startLocationTracking(): Flow<LocationData>
    suspend fun stopLocationTracking()
    suspend fun shareLocationWith(userId: String, location: LocationData)
    suspend fun getAvailableUsers(): List<User>
}
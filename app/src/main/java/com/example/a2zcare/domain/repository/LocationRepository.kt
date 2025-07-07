package com.example.a2zcare.domain.repository

import com.example.a2zcare.data.remote.response.LocationUser
import com.example.a2zcare.domain.entities.LocationData
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getCurrentLocation(): Flow<LocationData?>
    suspend fun getAvailableUsers(): List<LocationUser> // Changed to return domain User entities
    suspend fun searchUserByUsername(username: String): LocationUser?
    suspend fun sendLocationViaEmail(email: String, subject: String, body: String)
}
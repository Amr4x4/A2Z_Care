package com.example.a2zcare.domain.repository
/*
import com.example.a2zcare.data.model.ShareLocationRequest
import com.example.a2zcare.domain.entities.*
import com.example.a2zcare.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface LocationSharingRepository {

    // 🌍 Location
    suspend fun getLocationUpdates(): Flow<LocationData>
    suspend fun getCurrentLocation(): Result<LocationData>
    suspend fun shareLocation(request: ShareLocationRequest): Result<Unit>

    // 👤 User search & management
    suspend fun searchUsersByUsername(username: String): Result<List<User>>
    suspend fun searchUsersByEmail(email: String): Result<List<User>>
    suspend fun saveUser(userId: String, targetUserId: String): Result<Unit>
    suspend fun getSavedUsers(userId: String): Flow<List<SavedUser>>
    suspend fun removeSavedUser(userId: String, targetUserId: String): Result<Unit>

    // 🚨 Emergency Contacts
    suspend fun addEmergencyUser(userId: String, targetUserId: String): Result<Unit>
    suspend fun getEmergencyUsers(userId: String): Flow<List<EmergencyUser>>
    suspend fun removeEmergencyUser(userId: String, targetUserId: String): Result<Unit>

    // 📍 Emergency Alerts
    suspend fun sendEmergencyAlert(userId: String, location: LocationData, message: String): Result<Unit>

    // 📌 Utility
    suspend fun getAddressFromLocation(latitude: Double, longitude: Double): Result<String>
}

 */
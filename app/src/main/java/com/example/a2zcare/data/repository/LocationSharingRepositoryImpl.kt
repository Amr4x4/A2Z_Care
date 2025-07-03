package com.example.a2zcare.data.repository
/*
import android.location.Geocoder
import com.example.a2zcare.data.local.dao.EmergencyUserDao
import com.example.a2zcare.data.local.dao.SavedUserDao
import com.example.a2zcare.data.local.entity.EmergencyUserEntity
import com.example.a2zcare.data.local.entity.SavedUserEntity
import com.example.a2zcare.data.location.LocationClient
import com.example.a2zcare.data.model.*
import com.example.a2zcare.data.remote.api.HealthMonitoringApiService
import com.example.a2zcare.domain.entities.*
import com.example.a2zcare.domain.model.Result
import com.example.a2zcare.domain.repository.LocationSharingRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSharingRepositoryImpl @Inject constructor(
    private val apiService: HealthMonitoringApiService,
    private val locationClient: LocationClient,
    private val savedUserDao: SavedUserDao,
    private val emergencyUserDao: EmergencyUserDao,
    private val geocoder: Geocoder,
    private val gson: Gson
) : LocationSharingRepository {

    override suspend fun shareLocation(request: ShareLocationRequest): Result<Unit> {
        return try {
            // Use existing SMS or message endpoint to share location
            val locationMessage = "My current location: https://maps.google.com/?q=${request.latitude},${request.longitude}"
            val message = if (request.message != null) {
                "${request.message}\n\n$locationMessage"
            } else {
                locationMessage
            }

            val userResponse = apiService.getUserById(request.toUserId)
            if (userResponse.isSuccessful && userResponse.body()?.isSuccess == true) {
                val userData = gson.fromJson(userResponse.body()?.data, User::class.java)
                userData.phoneNumber?.let { phoneNumber ->
                    val smsRequest = SendSMSRequest(phoneNumber, message)
                    val smsResponse = apiService.sendSMSTwilio(smsRequest)
                    if (smsResponse.isSuccessful) {
                        Result.Success(Unit)
                    } else {
                        Result.Error("Failed to send location")
                    }
                } ?: Result.Error("User phone number not found")
            } else {
                Result.Error("User not found")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to share location")
        }
    }

    override suspend fun getLocationUpdates(): Flow<LocationData> {
        return locationClient.getLocationUpdate(10000L).map { location ->
            val address = getAddressFromLocationSync(location.latitude, location.longitude)
            LocationData(
                latitude = location.latitude,
                longitude = location.longitude,
                address = address
            )
        }
    }

    override suspend fun getCurrentLocation(): Result<LocationData> {
        return try {
            // This would require a single location request implementation
            // For now, we'll use the flow and take the first emission
            var currentLocation: LocationData? = null
            getLocationUpdates().collect { location ->
                currentLocation = location
                return@collect
            }
            currentLocation?.let { Result.Success(it) } ?: Result.Error("Unable to get current location")
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to get current location")
        }
    }

    override suspend fun searchUsersByUsername(username: String): Result<List<User>> {
        return try {
            val response = apiService.getUserByUsername(username)
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val userData = gson.fromJson(response.body()?.data, User::class.java)
                Result.Success(listOf(userData))
            } else {
                Result.Success(emptyList())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to search users")
        }
    }

    override suspend fun searchUsersByEmail(email: String): Result<List<User>> {
        return try {
            // Using the admin endpoint to get all users and filter by email
            val response = apiService.getAllUsers()
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                val allUsers = gson.fromJson(response.body()?.data, Array<User>::class.java).toList()
                val filteredUsers = allUsers.filter { it.email.contains(email, ignoreCase = true) }
                Result.Success(filteredUsers)
            } else {
                Result.Success(emptyList())
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to search users by email")
        }
    }

    override suspend fun saveUser(userId: String, targetUserId: String): Result<Unit> {
        return try {
            // Get user data first
            val userResponse = apiService.getUserById(targetUserId)
            if (userResponse.isSuccessful && userResponse.body()?.isSuccess == true) {
                val userData = gson.fromJson(userResponse.body()?.data, User::class.java)
                val savedUserEntity = SavedUserEntity(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    targetUserId = targetUserId,
                    targetUserName = userData.name,
                    targetUserEmail = userData.email,
                    targetUserPhone = userData.phoneNumber,
                    savedAt = System.currentTimeMillis()
                )
                savedUserDao.insertSavedUser(savedUserEntity)
                Result.Success(Unit)
            } else {
                Result.Error("User not found")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to save user")
        }
    }

    override suspend fun getSavedUsers(userId: String): Flow<List<SavedUser>> {
        return savedUserDao.getSavedUsers(userId).map { entities ->
            entities.map { entity ->
                SavedUser(
                    id = entity.id,
                    user = User(
                        id = entity.targetUserId,
                        name = entity.targetUserName,
                        firstName = "", // Would need to parse or store separately
                        lastName = "",
                        email = entity.targetUserEmail,
                        phoneNumber = entity.targetUserPhone
                    ),
                    savedAt = entity.savedAt
                )
            }
        }
    }

    override suspend fun removeSavedUser(userId: String, targetUserId: String): Result<Unit> {
        return try {
            savedUserDao.deleteSavedUser(userId, targetUserId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to remove saved user")
        }
    }

    override suspend fun addEmergencyUser(userId: String, targetUserId: String): Result<Unit> {
        return try {
            // Get user data first
            val userResponse = apiService.getUserById(targetUserId)
            if (userResponse.isSuccessful && userResponse.body()?.isSuccess == true) {
                val userData = gson.fromJson(userResponse.body()?.data, User::class.java)
                val emergencyUserEntity = EmergencyUserEntity(
                    id = UUID.randomUUID().toString(),
                    userId = userId,
                    targetUserId = targetUserId,
                    targetUserName = userData.name,
                    targetUserEmail = userData.email,
                    targetUserPhone = userData.phoneNumber,
                    addedAt = System.currentTimeMillis()
                )
                emergencyUserDao.insertEmergencyUser(emergencyUserEntity)
                Result.Success(Unit)
            } else {
                Result.Error("User not found")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to add emergency user")
        }
    }

    override suspend fun getEmergencyUsers(userId: String): Flow<List<EmergencyUser>> {
        return emergencyUserDao.getEmergencyUsers(userId).map { entities ->
            entities.map { entity ->
                EmergencyUser(
                    id = entity.id,
                    user = User(
                        id = entity.targetUserId,
                        name = entity.targetUserName,
                        firstName = "",
                        lastName = "",
                        email = entity.targetUserEmail,
                        phoneNumber = entity.targetUserPhone
                    ),
                    addedAt = entity.addedAt
                )
            }
        }
    }

    override suspend fun removeEmergencyUser(userId: String, targetUserId: String): Result<Unit> {
        return try {
            emergencyUserDao.deleteEmergencyUser(userId, targetUserId)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to remove emergency user")
        }
    }

    override suspend fun sendEmergencyAlert(userId: String, location: LocationData, message: String): Result<Unit> {
        return try {
            val emergencyUsers = emergencyUserDao.getEmergencyUsersSync(userId)
            val locationMessage = "🚨 EMERGENCY ALERT 🚨\n\n$message\n\nMy location: https://maps.google.com/?q=${location.latitude},${location.longitude}"

            var successCount = 0
            emergencyUsers.forEach { emergencyUser ->
                emergencyUser.targetUserPhone?.let { phoneNumber ->
                    try {
                        val smsRequest = SendSMSRequest(phoneNumber, locationMessage)
                        val response = apiService.sendSMSTwilio(smsRequest)
                        if (response.isSuccessful) {
                            successCount++
                        }
                    } catch (e: Exception) {
                        // Log error but continue with other contacts
                    }
                }
            }

            if (successCount > 0) {
                Result.Success(Unit)
            } else {
                Result.Error("Failed to send emergency alerts")
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to send emergency alert")
        }
    }

    override suspend fun getAddressFromLocation(latitude: Double, longitude: Double): Result<String> {
        return try {
            val address = getAddressFromLocationSync(latitude, longitude)
            Result.Success(address)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to get address")
        }
    }

    private fun getAddressFromLocationSync(latitude: Double, longitude: Double): String {
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
        } catch (e: Exception) {
            "Unknown location"
        }
    }
}
 */
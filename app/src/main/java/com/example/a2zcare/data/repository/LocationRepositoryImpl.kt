package com.example.a2zcare.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import com.example.a2zcare.data.remote.api.HealthMonitoringApiService
import com.example.a2zcare.data.remote.request.EmailRequest
import com.example.a2zcare.data.remote.response.LocationUser
import com.example.a2zcare.domain.entities.LocationData
import com.example.a2zcare.domain.repository.LocationRepository
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: HealthMonitoringApiService
) : LocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    override fun getCurrentLocation(): Flow<LocationData?> = callbackFlow {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val locationData = LocationData(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        address = null,
                        timestamp = System.currentTimeMillis()
                    )
                    trySend(locationData)
                }
            }
        }

        try {
            @SuppressLint("MissingPermission")
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                5000L
            ).build()

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.e("LocationRepo", "Location permission denied", e)
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    override suspend fun getAvailableUsers(): List<LocationUser> {
        return try {
            val response = apiService.getAllUsers()
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                // Convert data model Users to domain Users
                response.body()?.result?.map { dataUser ->
                    LocationUser(
                        id = dataUser.id,
                        userName = dataUser.userName,
                        email = dataUser.email
                    )
                } ?: emptyList()
            } else {
                Log.e("LocationRepo", "Failed to get users: ${response.message()}")
                getMockUsers()
            }
        } catch (e: Exception) {
            Log.e("LocationRepo", "Error getting users", e)
            getMockUsers()
        }
    }

    override suspend fun searchUserByUsername(username: String): LocationUser? {
        return try {
            val response = apiService.getUserByUsername(username)
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                response.body()?.result?.let { dataUser ->
                    LocationUser(
                        id = dataUser.id,
                        userName = dataUser.userName,
                        email = dataUser.email
                    )
                }
            } else {
                Log.e("LocationRepo", "Failed to search user: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            Log.e("LocationRepo", "Error searching user", e)
            null
        }
    }

    override suspend fun sendLocationViaEmail(email: String, subject: String, body: String) {
        return suspendCancellableCoroutine { continuation ->
            val emailRequest = EmailRequest(
                toEmail = email,
                subject = subject,
                body = body,
                attachments = null
            )

            // Launch a coroutine in a proper scope
            kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                try {
                    val response = apiService.sendEmail(emailRequest)
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        Log.d("LocationRepo", "Email sent successfully")
                        continuation.resume(Unit)
                    } else {
                        val error = "Failed to send email: ${response.message()}"
                        Log.e("LocationRepo", error)
                        continuation.resumeWithException(Exception(error))
                    }
                } catch (e: Exception) {
                    Log.e("LocationRepo", "Error sending email", e)
                    continuation.resumeWithException(e)
                }
            }
        }
    }

    private fun getMockUsers(): List<LocationUser> = listOf(
        LocationUser("1", "John Doe", "john@example.com"),
        LocationUser("2", "Jane Smith", "jane@example.com"),
        LocationUser("3", "Mike Johnson", "mike@example.com"),
        LocationUser("4", "Sarah Wilson", "sarah@example.com"),
        LocationUser("5", "David Brown", "david@example.com")
    )
}
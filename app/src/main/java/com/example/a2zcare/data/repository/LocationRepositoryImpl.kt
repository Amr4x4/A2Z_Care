package com.example.a2zcare.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.example.a2zcare.data.network.ApiService
import com.example.a2zcare.domain.entities.LocationData
import com.example.a2zcare.domain.entities.User
import com.example.a2zcare.domain.repository.LocationRepository
import com.example.a2zcare.util.hasLocationPermission
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class LocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiService
) : LocationRepository {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    override suspend fun getCurrentLocation(): Flow<LocationData?> = callbackFlow {
        if (!context.hasLocationPermission()) {
            Log.w("LocationRepo", "No location permission")
            trySend(null)
            close()
            return@callbackFlow
        }

        try {
            val lastLocation = try {
                fusedLocationClient.lastLocation.await()
            } catch (e: SecurityException) {
                Log.e("LocationRepo", "SecurityException while accessing lastLocation", e)
                null
            }

            if (lastLocation != null) {
                trySend(
                    LocationData(
                        latitude = lastLocation.latitude,
                        longitude = lastLocation.longitude,
                        timestamp = System.currentTimeMillis()
                    )
                )
            } else {
                val freshLocation = requestFreshLocation()
                trySend(
                    freshLocation?.let {
                        LocationData(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            timestamp = System.currentTimeMillis()
                        )
                    }
                )
            }
        } catch (e: Exception) {
            Log.e("LocationRepo", "getCurrentLocation error", e)
            trySend(null)
        }

        awaitClose { }
    }

    @SuppressLint("MissingPermission")
    private suspend fun requestFreshLocation(): Location? = suspendCancellableCoroutine { cont ->
        if (!context.hasLocationPermission()) {
            cont.resume(null)
            return@suspendCancellableCoroutine
        }

        try {
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
                .setMaxUpdates(1)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    fusedLocationClient.removeLocationUpdates(this)
                    cont.resume(result.lastLocation)
                }
            }

            cont.invokeOnCancellation {
                fusedLocationClient.removeLocationUpdates(callback)
            }

            try {
                fusedLocationClient.requestLocationUpdates(
                    request,
                    callback,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                Log.e("LocationRepo", "SecurityException in requestFreshLocation", e)
                cont.resume(null)
            }

        } catch (e: Exception) {
            Log.e("LocationRepo", "requestFreshLocation error", e)
            cont.resume(null)
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun startLocationTracking(): Flow<LocationData> = callbackFlow {
        if (!context.hasLocationPermission()) {
            Log.w("LocationRepo", "No permission for tracking")
            close()
            return@callbackFlow
        }

        try {
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
                .setMinUpdateIntervalMillis(2000L)
                .setGranularity(Granularity.GRANULARITY_PERMISSION_LEVEL)
                .build()

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let {
                        trySend(
                            LocationData(
                                latitude = it.latitude,
                                longitude = it.longitude,
                                timestamp = System.currentTimeMillis()
                            )
                        )
                    }
                }
            }

            try {
                fusedLocationClient.requestLocationUpdates(
                    request,
                    locationCallback!!,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                Log.e("LocationRepo", "SecurityException in startLocationTracking", e)
                close(e)
            }

            awaitClose {
                locationCallback?.let {
                    fusedLocationClient.removeLocationUpdates(it)
                    locationCallback = null
                }
            }
        } catch (e: Exception) {
            Log.e("LocationRepo", "startLocationTracking error", e)
            close(e)
        }
    }

    override suspend fun stopLocationTracking() {
        locationCallback?.let {
            try {
                fusedLocationClient.removeLocationUpdates(it)
            } catch (e: SecurityException) {
                Log.e("LocationRepo", "SecurityException during stopLocationTracking", e)
            }
            locationCallback = null
        }
    }

    override suspend fun shareLocationWith(userId: String, location: LocationData) {
        try {
            val response = apiService.shareLocation(userId, location)
            if (response.isSuccessful) {
                Log.d("LocationRepo", "Location shared with $userId")
            } else {
                Log.w("LocationRepo", "Failed to share location: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e("LocationRepo", "shareLocation error", e)
        }
    }

    override suspend fun getAvailableUsers(): List<User> {
        return try {
            val response = apiService.getAvailableUsers()
            if (response.isSuccessful) {
                response.body() ?: getMockUsers()
            } else {
                Log.w("LocationRepo", "API failed, returning mock users")
                getMockUsers()
            }
        } catch (e: Exception) {
            Log.e("LocationRepo", "getAvailableUsers error", e)
            getMockUsers()
        }
    }

    private fun getMockUsers(): List<User> = listOf(
        User("1", "John Doe", "john@example.com"),
        User("2", "Jane Smith", "jane@example.com"),
        User("3", "Mike Johnson", "mike@example.com"),
        User("4", "Sarah Wilson", "sarah@example.com"),
        User("5", "David Brown", "david@example.com")
    )
}

package com.example.a2zcare.data.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdate(interval: Long): Flow<Location>

    class LocationException(message: String): Exception(message)
}
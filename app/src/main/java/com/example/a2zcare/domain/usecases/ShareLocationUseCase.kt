package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.remote.response.LocationUser
import com.example.a2zcare.domain.entities.LocationData
import com.example.a2zcare.domain.repository.LocationRepository
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke() = locationRepository.getCurrentLocation()
}

class GetAvailableUsersUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke() = locationRepository.getAvailableUsers()
}

class SearchUserByUsernameUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(username: String): LocationUser? {
        return locationRepository.searchUserByUsername(username)
    }
}

class SendLocationViaEmailUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(email: String, location: LocationData) {
        val subject = "Location Share - A2Z Care"
        val mapsUrl = "https://maps.google.com/?q=${location.latitude},${location.longitude}"
        val body = """
            Hello,
            
            Someone has shared their location with you through A2Z Care app.
            
            Location Details:
            • Latitude: ${location.latitude}
            • Longitude: ${location.longitude}
            • Address: ${location.address ?: "Address not available"}
            • Timestamp: ${location.timestamp}
            
            You can view the location on Google Maps: $mapsUrl
            
            Best regards,
            A2Z Care Team
        """.trimIndent()

        locationRepository.sendLocationViaEmail(email, subject, body)
    }
}
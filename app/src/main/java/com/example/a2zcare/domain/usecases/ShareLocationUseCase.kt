package com.example.a2zcare.domain.usecases


import com.example.a2zcare.domain.entities.LocationData
import com.example.a2zcare.domain.repository.LocationRepository
import javax.inject.Inject

class ShareLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(userId: String, location: LocationData) {
        locationRepository.shareLocationWith(userId, location)
    }
}

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
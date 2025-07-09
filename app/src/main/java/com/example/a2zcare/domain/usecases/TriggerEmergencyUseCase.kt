package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.model.EmergencyContact2
import com.example.a2zcare.data.model.VitalSigns2
import com.example.a2zcare.domain.model.Result
import com.example.a2zcare.domain.repository.EmergencyRepository
import javax.inject.Inject

class TriggerEmergencyUseCase @Inject constructor(
    private val emergencyRepository: EmergencyRepository,
    locationService: LocationService
) {
    suspend operator fun invoke(
        vitalSigns: VitalSigns2,
        contacts: List<EmergencyContact2>
    ): Result<Unit> {
        return emergencyRepository.triggerEmergency(vitalSigns, contacts)
    }
}

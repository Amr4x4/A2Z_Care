package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.model.EmergencyContact2
import com.example.a2zcare.data.model.VitalSigns2
import com.example.a2zcare.domain.repository.EmergencyRepository
import javax.inject.Inject

class CheckCriticalVitalsUseCase @Inject constructor(
    private val emergencyRepository: EmergencyRepository
) {
    suspend operator fun invoke(vitals: VitalSigns2): CriticalVitalsResult {
        return if (EmergencyDetector.isCriticalCondition(vitals)) {
            val reason = EmergencyDetector.getCriticalReason(vitals)
            val contacts = emergencyRepository.getEmergencyContacts()
            CriticalVitalsResult.Critical(reason, contacts, vitals)
        } else {
            CriticalVitalsResult.Normal
        }
    }
}

sealed class CriticalVitalsResult {
    object Normal : CriticalVitalsResult()
    data class Critical(
        val reason: String,
        val contacts: List<EmergencyContact2>,
        val vitals: VitalSigns2
    ) : CriticalVitalsResult()
}

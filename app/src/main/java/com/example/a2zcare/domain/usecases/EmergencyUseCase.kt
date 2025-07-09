package com.example.a2zcare.domain.usecases

import com.example.a2zcare.data.model.EmergencyAlert
import com.example.a2zcare.domain.repository.EmergencyRepository
import com.example.a2zcare.util.LocationUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmergencyUseCase @Inject constructor(
    private val emergencyCallService: EmergencyCallService,
    private val smsService: SmsService,
    private val emergencyRepository: EmergencyRepository,
    private val locationUtils: LocationUtils
) {
    suspend fun triggerEmergencyProtocol(
        sbp: Int,
        dbp: Int,
        heartRate: Int,
        userId: String
    ) {
        val location = locationUtils.getCurrentLocation()
        val locationText = location?.let { locationUtils.formatLocationForMessage(it) } ?: "Location unavailable"

        val message = createEmergencyMessage(sbp, dbp, heartRate, locationText)

        // Call ambulance
        emergencyCallService.callAmbulance()

        // Get contacts
        val contacts = emergencyRepository.getEmergencyContacts()

        contacts.forEach { contact ->
            if (contact.phoneNumber.isNotBlank()) {
                smsService.sendEmergencySms(contact.phoneNumber, message)
            }

            // If needed, send to backend as well
            val alert = EmergencyAlert(
                phoneNumber = contact.phoneNumber,
                body = message
            )
            emergencyRepository.sendEmergencyAlert(alert)
        }
    }

    private fun createEmergencyMessage(
        sbp: Int,
        dbp: Int,
        heartRate: Int,
        location: String
    ): String {
        return """
            🚨 EMERGENCY ALERT 🚨
            
            Detected Critical Condition!
            • Blood Pressure: $sbp/$dbp mmHg
            • Heart Rate: $heartRate BPM
            • Time: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}
            • Location: $location
            
            A2Z Care is notifying your emergency contacts.
        """.trimIndent()
    }
}

package com.example.a2zcare.domain.repository

import com.example.a2zcare.data.model.EmergencyAlert
import com.example.a2zcare.data.model.EmergencyContact2
import com.example.a2zcare.data.model.VitalSigns2
import com.example.a2zcare.domain.model.Result

interface EmergencyRepository {
    suspend fun getEmergencyContacts(): List<EmergencyContact2>
    suspend fun saveEmergencyContact(contact: EmergencyContact2)
    suspend fun deleteEmergencyContact(id: Int)
    suspend fun sendEmergencyAlert(alert: EmergencyAlert): com.example.a2zcare.domain.model.Result<Boolean>
    suspend fun triggerEmergency(
        vitalSigns: VitalSigns2,
        contacts: List<EmergencyContact2>
    ): Result<Unit>


}

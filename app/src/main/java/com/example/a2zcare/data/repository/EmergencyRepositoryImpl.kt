package com.example.a2zcare.data.repository

import com.example.a2zcare.data.local.dao.EmergencyDao
import com.example.a2zcare.data.mapper.toDomain
import com.example.a2zcare.data.mapper.toEntity
import com.example.a2zcare.data.model.EmergencyAlert
import com.example.a2zcare.data.model.EmergencyContact2
import com.example.a2zcare.data.model.VitalSigns2
import com.example.a2zcare.domain.model.Result
import com.example.a2zcare.data.remote.api.HealthMonitoringApiService
import com.example.a2zcare.domain.repository.EmergencyRepository
import javax.inject.Inject

class EmergencyRepositoryImpl @Inject constructor(
    private val emergencyDao: EmergencyDao,
    private val emergencyApi: HealthMonitoringApiService.EmergencyApi
) : EmergencyRepository {

    override suspend fun getEmergencyContacts(): List<EmergencyContact2> {
        return emergencyDao.getAllContacts().map { it.toDomain() }
    }

    override suspend fun saveEmergencyContact(contact: EmergencyContact2) {
        emergencyDao.insertContact(contact.toEntity())
    }

    override suspend fun deleteEmergencyContact(id: Int) {
        emergencyDao.deleteContactById(id)
    }

    override suspend fun sendEmergencyAlert(alert: EmergencyAlert): Result<Boolean> {
        return try {
            val response = emergencyApi.sendAlert(alert)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(message = "Failed to send alert: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error(message = "Network error: ${e.message}")
        }
    }

    override suspend fun triggerEmergency(
        vitalSigns: VitalSigns2,
        contacts: List<EmergencyContact2>
    ): Result<Unit> {
        return try {
            val contactNumbers = contacts.map { it.phoneNumber }
            val response = emergencyApi.sendEmergencyAlert(
                systolic = vitalSigns.systolicBP,
                diastolic = vitalSigns.diastolicBP,
                heartRate = vitalSigns.heartRate,
                contacts = contactNumbers
            )
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Emergency alert failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Exception: ${e.message}")
        }
    }
}

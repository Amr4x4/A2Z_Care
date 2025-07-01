package com.example.a2zcare.domain.repository

import com.example.a2zcare.data.model.ActivityPredictionRequest
import com.example.a2zcare.data.model.ConnectContactRequest
import com.example.a2zcare.data.model.EmergencyContactRequest
import com.example.a2zcare.data.model.LoginRequest
import com.example.a2zcare.data.model.RegisterRequest
import com.example.a2zcare.data.model.ResetPasswordRequest
import com.example.a2zcare.data.model.SendMessageRequest
import com.example.a2zcare.data.model.SendSMSRequest
import com.example.a2zcare.data.model.SensorDataRequest
import com.example.a2zcare.data.model.UpdateUserRequest
import com.example.a2zcare.data.model.UserWithEmergencyContacts
import com.example.a2zcare.domain.model.Result

interface HealthMonitoringRepository {
    // User Authentication
    suspend fun register(request: RegisterRequest): Result<Unit>
    suspend fun login(request: LoginRequest): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun updateUser(id: String, request: UpdateUserRequest): Result<String>
    suspend fun getUserData(userId: String): Result<String>
    suspend fun resetPassword(request: ResetPasswordRequest): Result<Unit>
    suspend fun forgotPassword(email: String): Result<Unit>

    // Emergency Contacts
    suspend fun createEmergencyContact(userId: String, request: EmergencyContactRequest): Result<String>
    suspend fun getEmergencyContacts(userId: String): Result<UserWithEmergencyContacts>
    suspend fun connectContact(request: ConnectContactRequest): Result<Unit>
    suspend fun disconnectContact(userId: String, contactId: Int): Result<Unit>

    // Sensor Data
    suspend fun importSensorData(userId: String, request: SensorDataRequest): Result<Unit>
    suspend fun getCachedSensorData(userId: String): Result<Unit>

    // Activity Data
    suspend fun predictActivity(request: ActivityPredictionRequest): Result<String>
    suspend fun getActivityDataByUser(userId: String): Result<String>
    suspend fun getLatestActivityData(userId: String): Result<String>

    // Predictions
    suspend fun sendBloodPressureAI(userId: String, batchSize: Int): Result<String>
    suspend fun getLatestBloodPressurePrediction(userId: String): Result<String>
    suspend fun sendHeartDiseaseAI(userId: String, batchSize: Int): Result<String>
    suspend fun getLatestHeartDiseasePrediction(userId: String): Result<String>
    suspend fun sendHeartRateAI(userId: String, batchSize: Int): Result<String>
    suspend fun getLatestHeartRateCalculation(userId: String): Result<String>

    // SMS
    suspend fun sendSMS(request: SendSMSRequest): Result<Unit>
    suspend fun sendMessage(request: SendMessageRequest): Result<Unit>
}

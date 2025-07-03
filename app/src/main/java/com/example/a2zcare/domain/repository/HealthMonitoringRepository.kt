package com.example.a2zcare.domain.repository

import com.example.a2zcare.data.model.*
import com.example.a2zcare.data.remote.request.LoginRequest
import com.example.a2zcare.data.remote.request.RegisterRequest
import com.example.a2zcare.data.remote.request.ResetPasswordRequest
import com.example.a2zcare.data.remote.request.SendEmailRequest
import com.example.a2zcare.data.remote.request.UpdateUserRequest
import com.example.a2zcare.data.remote.response.LoginResponse
import com.example.a2zcare.data.remote.response.RegisterResponse
import com.example.a2zcare.data.remote.response.SendEmailResponse
import com.example.a2zcare.data.remote.response.UpdateUserResponse
import com.example.a2zcare.domain.model.Result

interface HealthMonitoringRepository {
    suspend fun register(request: RegisterRequest): Result<RegisterResponse>
    suspend fun login(request: LoginRequest): Result<LoginResponse>
    suspend fun getUserData(userId: String): Result<User>
    suspend fun updateUser(id: String, request: UpdateUserRequest): Result<UpdateUserResponse>
    suspend fun resetPassword(request: ResetPasswordRequest): Result<Unit>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun sendEmail(request: SendEmailRequest): Result<SendEmailResponse>

    // Additional methods that match your implementation
    suspend fun getAllUsers(): Result<String>
    suspend fun getUserById(userId: String): Result<String>
    suspend fun getUserByUsername(username: String): Result<String>

    // Emergency Contacts
    suspend fun createEmergencyContact(
        userId: String,
        request: EmergencyContactRequest
    ): Result<String>

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
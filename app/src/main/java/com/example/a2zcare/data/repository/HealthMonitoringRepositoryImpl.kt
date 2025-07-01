package com.example.a2zcare.data.repository

import com.example.a2zcare.data.model.*
import com.example.a2zcare.data.remote.api.HealthMonitoringApiService
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.domain.model.Result
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthMonitoringRepositoryImpl @Inject constructor(
    private val apiService: HealthMonitoringApiService,
    private val tokenManager: TokenManager
) : HealthMonitoringRepository {

    override suspend fun register(request: RegisterRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.register(request)
            if (response.isSuccessful) {
                Result.Success<Unit>(Unit)
            } else {
                Result.Error("Registration failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun login(request: LoginRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.login(request)
            if (response.isSuccessful) {
                val token = response.headers()["Authorization"] ?: response.headers()["Token"]
                token?.let { tokenManager.saveToken(it) }
                Result.Success<Unit>(Unit)
            } else {
                Result.Error("Login failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun logout(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val userId = tokenManager.getUserId()
            val token = tokenManager.getToken()
            val deviceId = tokenManager.getDeviceId()
            if (userId != null && token != null && deviceId != null) {
                val response = apiService.logout(LogoutRequest(userId, token, deviceId))
                if (response.isSuccessful) {
                    tokenManager.clearAllTokens()
                    Result.Success<Unit>(Unit)
                } else {
                    Result.Error("Logout failed: ${response.message()}")
                }
            } else {
                tokenManager.clearAllTokens()
                Result.Success<Unit>(Unit)
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun updateUser(id: String, request: UpdateUserRequest): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.updateUser(id, request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true) {
                    Result.Success<String>(body.result ?: "")
                } else {
                    Result.Error("Update failed: ${body?.errors?.joinToString()}")
                }
            } else {
                Result.Error("Update failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getUserData(userId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserData(userId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true) {
                    Result.Success<String>(body.result ?: "")
                } else {
                    Result.Error("Failed to get user data: ${body?.errors?.joinToString()}")
                }
            } else {
                Result.Error("Failed to get user data: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.resetPassword(request)
            if (response.isSuccessful) {
                Result.Success<Unit>(Unit)
            } else {
                Result.Error("Password reset failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun forgotPassword(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.forgotPassword(email)
            if (response.isSuccessful) {
                Result.Success<Unit>(Unit)
            } else {
                Result.Error("Forgot password failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun createEmergencyContact(userId: String, request: EmergencyContactRequest): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.createEmergencyContact(userId, request)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success<String>(body.result ?: "")
            } else {
                Result.Error("Failed to create emergency contact: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getEmergencyContacts(userId: String): Result<UserWithEmergencyContacts> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getEmergencyContacts(userId)
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success<UserWithEmergencyContacts>(body)
            } else {
                Result.Error("Failed to get emergency contacts: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun connectContact(request: ConnectContactRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.connectContact(request)
            if (response.isSuccessful) {
                Result.Success<Unit>(Unit)
            } else {
                Result.Error("Failed to connect contact: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun disconnectContact(userId: String, contactId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.disconnectContact(userId, contactId)
            if (response.isSuccessful) {
                Result.Success<Unit>(Unit)
            } else {
                Result.Error("Failed to disconnect contact: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun importSensorData(userId: String, request: SensorDataRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.importSensorData(userId, request)
            if (response.isSuccessful) {
                Result.Success<Unit>(Unit)
            } else {
                Result.Error("Failed to import sensor data: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getCachedSensorData(userId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getCachedSensorData(userId)
            if (response.isSuccessful) {
                Result.Success<Unit>(Unit)
            } else {
                Result.Error("Failed to get cached sensor data: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun predictActivity(request: ActivityPredictionRequest): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.predictActivity(request)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success<String>(body.result ?: "")
            } else {
                Result.Error("Activity prediction failed: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getActivityDataByUser(userId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getActivityDataByUser(userId)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success<String>(body.result ?: "")
            } else {
                Result.Error("Failed to get activity data: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getLatestActivityData(userId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLatestActivityData(userId)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success<String>(body.result ?: "")
            } else {
                Result.Error("Failed to get latest activity data: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun sendBloodPressureAI(userId: String, batchSize: Int): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.sendBloodPressureAI(userId, batchSize)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success<String>(body.result ?: "")
            } else {
                Result.Error("Blood pressure AI failed: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getLatestBloodPressurePrediction(userId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLatestBloodPressurePrediction(userId)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success<String>(body.result ?: "")
            } else {
                Result.Error("Failed to get blood pressure prediction: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun sendHeartDiseaseAI(userId: String, batchSize: Int): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.sendHeartDiseaseAI(userId, batchSize)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success<String>(body.result ?: "")
            } else {
                Result.Error("Heart disease AI failed: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getLatestHeartDiseasePrediction(userId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLatestHeartDiseasePrediction(userId)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success<String>(body.result ?: "")
            } else {
                Result.Error("Failed to get heart disease prediction: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun sendHeartRateAI(userId: String, batchSize: Int): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.sendHeartRateAI(userId, batchSize)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success<String>(body.result ?: "")
            } else {
                Result.Error("Heart rate AI failed: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getLatestHeartRateCalculation(userId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLatestHeartRateCalculation(userId)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success<String>(body.result ?: "")
            } else {
                Result.Error("Failed to get heart rate calculation: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun sendSMS(request: SendSMSRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.sendSMSTwilio(request)
            if (response.isSuccessful) {
                Result.Success<Unit>(Unit)
            } else {
                Result.Error("Failed to send SMS: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun sendMessage(request: SendMessageRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.sendMessage(request)
            if (response.isSuccessful) {
                Result.Success<Unit>(Unit)
            } else {
                Result.Error("Failed to send message: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }
}

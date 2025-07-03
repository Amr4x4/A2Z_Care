package com.example.a2zcare.data.repository

import com.example.a2zcare.data.model.ActivityPredictionRequest
import com.example.a2zcare.data.model.ConnectContactRequest
import com.example.a2zcare.data.model.EmergencyContactRequest
import com.example.a2zcare.data.model.SendMessageRequest
import com.example.a2zcare.data.model.SendSMSRequest
import com.example.a2zcare.data.model.SensorDataRequest
import com.example.a2zcare.data.model.User
import com.example.a2zcare.data.model.UserWithEmergencyContacts
import com.example.a2zcare.data.remote.api.HealthMonitoringApiService
import com.example.a2zcare.data.remote.request.LoginRequest
import com.example.a2zcare.data.remote.request.RegisterRequest
import com.example.a2zcare.data.remote.request.ResetPasswordRequest
import com.example.a2zcare.data.remote.request.SendEmailRequest
import com.example.a2zcare.data.remote.request.UpdateUserRequest
import com.example.a2zcare.data.remote.response.LoginResponse
import com.example.a2zcare.data.remote.response.RegisterResponse
import com.example.a2zcare.data.remote.response.SendEmailResponse
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.data.remote.response.UpdateUserResponse
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

    override suspend fun register(request: RegisterRequest): Result<RegisterResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.register(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true && body.result != null) {
                    // Save user ID for future use
                    tokenManager.saveUserId(body.result.id)
                    Result.Success(body.result)
                } else {
                    Result.Error("Registration failed: ${body?.errors?.joinToString()}")
                }
            } else {
                Result.Error("Registration failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun login(request: LoginRequest): Result<LoginResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.login(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true && body.result != null) {
                    tokenManager.saveToken(body.result.token)
                    tokenManager.saveUserId(body.result.user.id)
                    tokenManager.saveUserData(body.result.user)
                    Result.Success(body.result)
                } else {
                    Result.Error("Login failed: ${body?.errors?.joinToString()}")
                }
            } else {
                Result.Error("Login failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun updateUser(id: String, request: UpdateUserRequest): Result<UpdateUserResponse> = withContext(Dispatchers.IO) {
        try {
            println("updateUser() - Request: $request")
            val response = apiService.updateUser(id, request)
            val errorBodyString = response.errorBody()?.string()
            println("updateUser() - Raw response: code=${response.code()}, message=${response.message()}, body=${response.body()}, errorBody=$errorBodyString")
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true && body.result != null) {
                    Result.Success(body.result)
                } else {
                    Result.Error("Update failed: ${body?.errors?.joinToString() ?: "Unknown error"}")
                }
            } else {
                // Print error body for debugging
                Result.Error("Update failed: ${response.message()}${if (!errorBodyString.isNullOrBlank()) " | $errorBodyString" else ""}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getUserData(userId: String): Result<User> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserData(userId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true && body.result != null) {
                    // Update stored user data
                    tokenManager.saveUserData(body.result)
                    Result.Success(body.result)
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
                Result.Success(Unit)
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
                Result.Success(Unit)
            } else {
                Result.Error("Forgot password failed: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun sendEmail(request: SendEmailRequest): Result<SendEmailResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.sendEmail(request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true && body.result != null) {
                    Result.Success(body.result)
                } else {
                    Result.Error("Failed to send email: ${body?.errors?.joinToString()}")
                }
            } else {
                Result.Error("Failed to send email: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    // Keep all the existing methods from your original repository for backward compatibility
    override suspend fun getAllUsers(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllUsers()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true) {
                    Result.Success<String>(body.result ?: "")
                } else {
                    Result.Error("Failed to get users: ${body?.errors?.joinToString()}")
                }
            } else {
                Result.Error("Failed to get users: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getUserById(userId: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserById(userId)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true) {
                    Result.Success<String>(body.result ?: "")
                } else {
                    Result.Error("Failed to get user by ID: ${body?.errors?.joinToString()}")
                }
            } else {
                Result.Error("Failed to get user by ID: ${response.message()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun getUserByUsername(username: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserByUsername(username)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true) {
                    Result.Success<String>(body.result ?: "")
                } else {
                    Result.Error("Failed to get user by username: ${body?.errors?.joinToString()}")
                }
            } else {
                Result.Error("Failed to get user by username: ${response.message()}")
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

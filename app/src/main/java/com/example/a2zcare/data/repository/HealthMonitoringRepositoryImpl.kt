package com.example.a2zcare.data.repository

import android.util.Log
import com.example.a2zcare.data.model.ActivityPredictionRequest
import com.example.a2zcare.data.model.ConnectContactRequest
import com.example.a2zcare.data.model.EmergencyContactRequest
import com.example.a2zcare.data.model.SendMessageRequest
import com.example.a2zcare.data.model.SendSMSRequest
import com.example.a2zcare.data.model.User
import com.example.a2zcare.data.model.UserWithEmergencyContacts
import com.example.a2zcare.data.remote.api.HealthMonitoringApiService
import com.example.a2zcare.data.remote.request.BloodPressureResult
import com.example.a2zcare.data.remote.request.HeartRateResult
import com.example.a2zcare.data.remote.request.LoginRequest
import com.example.a2zcare.data.remote.request.RegisterRequest
import com.example.a2zcare.data.remote.request.ResetPasswordRequest
import com.example.a2zcare.data.remote.request.SendEmailRequest
import com.example.a2zcare.data.remote.request.UpdateUserRequest
import com.example.a2zcare.data.remote.response.HeartDiseasePredictionResponse
import com.example.a2zcare.data.remote.response.LoginResponse
import com.example.a2zcare.data.remote.response.RegisterResponse
import com.example.a2zcare.data.remote.response.SendEmailResponse
import com.example.a2zcare.data.remote.response.SensorDataImportResponse
import com.google.gson.Gson
import com.example.a2zcare.data.remote.response.TokenManager
import com.example.a2zcare.data.remote.response.UpdateUserResponse
import com.example.a2zcare.domain.model.Result
import com.example.a2zcare.domain.repository.HealthMonitoringRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
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

            // Log the response for debugging
            Log.d("ResetPassword", "Response code: ${response.code()}")
            Log.d("ResetPassword", "Response headers: ${response.headers()}")

            if (response.isSuccessful) {
                val responseBody = response.body()?.string()
                Log.d("ResetPassword", "Response body: $responseBody")

                if (responseBody?.contains("Password Changed Correctly", ignoreCase = true) == true) {
                    Result.Success(Unit)
                } else {
                    Result.Error("Unexpected response format")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("ResetPassword", "Error response: $errorBody")

                // Try to parse error as JSON, fallback to plain text
                val errorMessage = try {
                    // If your error responses are JSON, parse them here
                    errorBody ?: "Password reset failed"
                } catch (e: Exception) {
                    errorBody ?: "Password reset failed"
                }

                Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Log.e("ResetPassword", "Network error", e)
            Result.Error("Network error: ${e.message ?: "Unknown error"}")
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

    override suspend fun getAllUsers(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAllUsers()
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.isSuccess == true) {
                    Result.Success<String>((body.result ?: "") as String)
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
                    Result.Success<String>((body.result ?: "") as String)
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

    override suspend fun importSensorDataFile(
        userId: String,
        file: MultipartBody.Part
    ): Result<SensorDataImportResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.importSensorDataFile(userId, file)
            if (response.isSuccessful) {
                response.body()?.let { responseBody ->
                    Result.Success(responseBody)
                } ?: Result.Error("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                Result.Error("Failed to import sensor data: ${response.message()}${if (!errorBody.isNullOrBlank()) " | $errorBody" else ""}")
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

    override suspend fun sendBloodPressureAI(userId: String, batchSize: Int): Result<BloodPressureResult> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.sendBloodPressureAI(userId, batchSize)
                val body = response.body()
                if (response.isSuccessful && body?.isSuccess == true) {
                    Result.Success(body.result ?: BloodPressureResult(0.0, 0.0, "Unknown"))
                } else {
                    Result.Error("Blood pressure AI failed: ${body?.errors?.joinToString()}")
                }
            } catch (e: Exception) {
                Result.Error("Network error: ${e.message}", e)
            }
        }

    override suspend fun getLatestBloodPressurePrediction(userId: String): Result<BloodPressureResult> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getLatestBloodPressurePrediction(userId)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true && body.result != null) {
                // Fix: If result is already an object, use it directly; if it's a string, parse it.
                val bpResult: BloodPressureResult = when (body.result) {
                    is BloodPressureResult -> body.result
                    is Map<*, *> -> Gson().fromJson(Gson().toJson(body.result), BloodPressureResult::class.java)
                    is String -> Gson().fromJson(body.result, BloodPressureResult::class.java)
                    else -> throw IllegalStateException("Unexpected result type: ${body.result?.javaClass}")
                }
                Result.Success(bpResult)
            } else {
                Result.Error("Failed to get blood pressure prediction: ${body?.errors?.joinToString()}")
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}", e)
        }
    }

    override suspend fun sendHeartDiseaseAI(userId: String, batchSize: Int): Result<HeartDiseasePredictionResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.sendHeartDiseaseAI(userId, batchSize)
                val body = response.body()

                if (response.isSuccessful && body?.isSuccess == true && body.result != null) {
                    // Extract the nested prediction correctly
                    val prediction = body.result.prediction.prediction
                    val unifiedResponse = HeartDiseasePredictionResponse(prediction)
                    Result.Success(unifiedResponse)
                } else {
                    val errorMessage = body?.errors?.joinToString() ?: "Unknown error"
                    Result.Error("Heart disease AI failed: $errorMessage")
                }
            } catch (e: Exception) {
                Result.Error("Network error: ${e.message}", e)
            }
        }

    override suspend fun getLatestHeartDiseasePrediction(userId: String): Result<HeartDiseasePredictionResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getLatestHeartDiseasePrediction(userId)
                val body = response.body()

                if (response.isSuccessful && body?.isSuccess == true && body.result != null) {
                    // Extract diseases field and convert to unified format
                    val prediction = body.result.diseases
                    val unifiedResponse = HeartDiseasePredictionResponse(prediction)
                    Result.Success(unifiedResponse)
                } else {
                    val errorMessage = body?.errors?.joinToString() ?: "Unknown error"
                    Result.Error("Failed to get heart disease prediction: $errorMessage")
                }
            } catch (e: Exception) {
                Result.Error("Network error: ${e.message}", e)
            }
        }

    override suspend fun sendHeartRateAI(userId: String, batchSize: Int): Result<HeartRateResult> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.sendHeartRateAI(userId, batchSize)
            val body = response.body()
            if (response.isSuccessful && body?.isSuccess == true) {
                Result.Success(body.result ?: HeartRateResult(0, "Unknown"))
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

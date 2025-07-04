package com.example.a2zcare.data.remote.api

import com.example.a2zcare.data.model.ActivityPredictionRequest
import com.example.a2zcare.data.remote.response.ApiResponse
import com.example.a2zcare.data.model.ConnectContactRequest
import com.example.a2zcare.data.model.EmergencyContact
import com.example.a2zcare.data.model.EmergencyContactRequest
import com.example.a2zcare.data.model.SendMessageRequest
import com.example.a2zcare.data.model.SendSMSRequest
import com.example.a2zcare.data.model.SensorDataRequest
import com.example.a2zcare.data.model.User
import com.example.a2zcare.data.model.UserWithEmergencyContacts
import com.example.a2zcare.data.remote.request.BloodPressureResult
import com.example.a2zcare.data.remote.request.HeartRateResult
import com.example.a2zcare.data.remote.request.LoginRequest
import com.example.a2zcare.data.remote.request.RegisterRequest
import com.example.a2zcare.data.remote.request.ResetPasswordRequest
import com.example.a2zcare.data.remote.request.SendEmailRequest
import com.example.a2zcare.data.remote.request.UpdateUserRequest
import com.example.a2zcare.data.remote.response.HeartDiseaseAIResponse
import com.example.a2zcare.data.remote.response.HeartDiseaseLatestResponse
import com.example.a2zcare.data.remote.response.LoginResponse
import com.example.a2zcare.data.remote.response.RegisterResponse
import com.example.a2zcare.data.remote.response.SendEmailResponse
import com.example.a2zcare.data.remote.response.SensorDataImportResponse
import com.example.a2zcare.data.remote.response.UpdateUserResponse
import com.example.a2zcare.domain.entities.LocationData
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface HealthMonitoringApiService {
    // User Authentication Endpoints
    @POST("api/Users/Register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<RegisterResponse>>

    @POST("api/Users/Login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    // If your backend expects /api/Users/Update{id} (no slash before {id}), use this:
    @PUT("api/Users/Update{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body request: UpdateUserRequest
    ): Response<ApiResponse<UpdateUserResponse>>

    @GET("api/Users/userdata/by-id/{userId}")
    suspend fun getUserData(@Path("userId") userId: String): Response<ApiResponse<User>>

    @POST("api/Users/ResetPassword")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<String>

    @POST("api/Users/ForgotPassword")
    suspend fun forgotPassword(@Query("email") email: String): Response<String>

    @POST("api/Users/Send-Email")
    suspend fun sendEmail(@Body request: SendEmailRequest): Response<ApiResponse<SendEmailResponse>>

    // Admin Endpoints
    @GET("api/Admin/users")
    suspend fun getAllUsers(): Response<ApiResponse<String>>

    @GET("api/Admin/user/by-id/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): Response<ApiResponse<String>>

    @GET("api/Admin/user/by-username/{username}")
    suspend fun getUserByUsername(@Path("username") username: String): Response<ApiResponse<String>>

    @DELETE("api/Admin/sensor-data/user/{userId}")
    suspend fun deleteSensorData(@Path("userId") userId: String): Response<ApiResponse<String>>

    // Emergency Contact Endpoints
    @POST("api/EmergencyContact")
    suspend fun createEmergencyContact(
        @Query("Userid") userId: String,
        @Body request: EmergencyContactRequest
    ): Response<ApiResponse<String>>

    @GET("api/EmergencyContact/{userId}/emergency-contacts")
    suspend fun getEmergencyContacts(@Path("userId") userId: String): Response<UserWithEmergencyContacts>

    @GET("api/EmergencyContact/{email}/get-emergency-contacts")
    suspend fun getEmergencyContactsByEmail(@Path("email") email: String): Response<UserWithEmergencyContacts>

    @GET("api/EmergencyContact/{contactId}/get-users")
    suspend fun getUsersByContactId(@Path("contactId") contactId: Int): Response<List<EmergencyContact>>

    @GET("api/EmergencyContact/{id}/emergency-contact-with-users")
    suspend fun getEmergencyContactWithUsers(@Path("id") id: Int): Response<EmergencyContact>

    @POST("api/EmergencyContact/connect")
    suspend fun connectContact(@Body request: ConnectContactRequest): Response<Unit>

    @DELETE("api/EmergencyContact/disconnect/{userId}/{contactId}")
    suspend fun disconnectContact(
        @Path("userId") userId: String,
        @Path("contactId") contactId: Int
    ): Response<Unit>

    // Sensor Data Endpoints
    @POST("api/SensorDataScreen/import")
    suspend fun importSensorData(
        @Query("userId") userId: String,
        @Body request: SensorDataRequest
    ): Response<Unit>

    @Multipart
    @POST("api/SensorData/import/file")
    suspend fun importSensorDataFile(
        @Query("userId") userId: String,
        @Part file: MultipartBody.Part
    ): Response<SensorDataImportResponse>

    @GET("api/SensorDataScreen/cached/{userId}")
    suspend fun getCachedSensorData(@Path("userId") userId: String): Response<Unit>

    @GET("api/SensorDataScreen/Get-ById")
    suspend fun getSensorDataById(@Query("userid") userId: String): Response<Unit>

    // Activity Data Endpoints
    @Multipart
    @POST("api/ActivityDataModel/importActivity/file")
    suspend fun importActivityFile(
        @Query("userId") userId: String,
        @Part file: MultipartBody.Part
    ): Response<Unit>

    @POST("api/ActivityDataModel/predict")
    suspend fun predictActivity(@Body request: ActivityPredictionRequest): Response<ApiResponse<String>>

    @GET("api/ActivityDataModel/user/{userId}")
    suspend fun getActivityDataByUser(@Path("userId") userId: String): Response<ApiResponse<String>>

    @GET("api/ActivityDataModel/GetLatestByUserId/{userId}")
    suspend fun getLatestActivityData(@Path("userId") userId: String): Response<ApiResponse<String>>

    @GET("api/ActivityDataModel/GetRangeByUserId/{userId}")
    suspend fun getActivityDataRange(
        @Path("userId") userId: String,
        @Query("startdate") startDate: String,
        @Query("enddate") endDate: String
    ): Response<ApiResponse<String>>

    @PUT("api/ActivityDataModel/{id}/weight")
    suspend fun updateWeight(
        @Path("id") id: String,
        @Body weight: Double
    ): Response<Unit>

    // Blood Pressure Prediction Endpoints
    @POST("api/BloodPressurePrediction/send-ai")
    suspend fun sendBloodPressureAI(
        @Query("userId") userId: String,
        @Query("batchsize") batchSize: Int
    ): Response<ApiResponse<BloodPressureResult>>

    @GET("api/BloodPressurePrediction/GetAllByUserId/{userId}")
    suspend fun getAllBloodPressurePredictions(@Path("userId") userId: String): Response<ApiResponse<String>>

    @GET("api/BloodPressurePrediction/GetLatestByUserId/{userId}")
    suspend fun getLatestBloodPressurePrediction(@Path("userId") userId: String): Response<ApiResponse<String>>

    @GET("api/BloodPressurePrediction/GetRangeByUserId/{userId}")
    suspend fun getBloodPressurePredictionRange(
        @Path("userId") userId: String,
        @Query("startdate") startDate: String,
        @Query("enddate") endDate: String
    ): Response<ApiResponse<String>>

    // Heart Disease Prediction Endpoints
    @POST("api/HeartDiseasePrediction/send-ai")
    suspend fun sendHeartDiseaseAI(
        @Query("userId") userId: String,
        @Query("batchsize") batchSize: Int
    ): Response<HeartDiseaseAIResponse>  // <- Changed response type

    @GET("api/HeartDiseasePrediction/GetLatestByUserId/{userId}")
    suspend fun getLatestHeartDiseasePrediction(
        @Path("userId") userId: String
    ): Response<HeartDiseaseLatestResponse>  // <- Changed response ty

    @GET("api/HeartDiseasePrediction/GetAllByUserId/{userId}")
    suspend fun getAllHeartDiseasePredictions(@Path("userId") userId: String): Response<ApiResponse<String>>

    @GET("api/HeartDiseasePrediction/GetLatestByUserId/{userId}")
    suspend fun getHeartDiseasePredictionRange(
        @Path("userId") userId: String,
        @Query("startdate") startDate: String,
        @Query("enddate") endDate: String
    ): Response<ApiResponse<String>>

    // Heart Rate Calculation Endpoints
    @POST("api/HeartRateCalculation/send-ai")
    suspend fun sendHeartRateAI(
        @Query("userId") userId: String,
        @Query("batchsize") batchSize: Int
    ): Response<ApiResponse<HeartRateResult>>

    @GET("api/HeartRateCalculation/GetAllByUserId/{userId}")
    suspend fun getAllHeartRateCalculations(@Path("userId") userId: String): Response<ApiResponse<String>>

    @GET("api/HeartRateCalculation/GetLatestByUserId/{userId}")
    suspend fun getLatestHeartRateCalculation(@Path("userId") userId: String): Response<ApiResponse<String>>

    @GET("api/HeartRateCalculation/GetRangeByUserId/{userId}")
    suspend fun getHeartRateCalculationRange(
        @Path("userId") userId: String,
        @Query("startdate") startDate: String,
        @Query("enddate") endDate: String
    ): Response<ApiResponse<String>>

    // SMS Endpoints
    @POST("api/SMS/SendSMS/twilio")
    suspend fun sendSMSTwilio(@Body request: SendSMSRequest): Response<Unit>

    @POST("api/SMS/SendMessage")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<Unit>

    @POST("location/share/{userId}")
    suspend fun shareLocation(
        @Path("userId") userId: String,
        @Body locationData: LocationData
    ): Response<Unit>

}
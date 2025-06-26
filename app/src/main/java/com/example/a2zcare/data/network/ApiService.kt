package com.example.a2zcare.data.network


import com.example.a2zcare.domain.entities.LocationData
import com.example.a2zcare.domain.entities.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class SignUpRequest(
    val userName: String,
    val password: String,
    val email: String,
    val role: Int = 0
)

data class SignUpResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String? = null,
    val token: String? = null
)

interface ApiService {
    @POST("signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<SignUpResponse>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("location/share/{userId}")
    suspend fun shareLocation(
        @Path("userId") userId: String,
        @Body locationData: LocationData
    ): Response<Unit>

    @GET("users")
    suspend fun getAvailableUsers(): Response<List<User>>
}

package com.example.a2zcare.data.network.api

import com.example.a2zcare.data.model.UserResponse
import com.example.a2zcare.domain.entities.LocationData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("location/share/{userId}")
    suspend fun shareLocation(
        @Path("userId") userId: String,
        @Body locationData: LocationData
    ): Response<Unit>

    @GET("api/Admin/user/by-username/{username}")
    suspend fun getUserByUsername(
        @Path("username") username: String): Response<UserResponse>
}
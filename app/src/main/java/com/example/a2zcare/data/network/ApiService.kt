package com.example.a2zcare.data.network


import com.example.a2zcare.domain.entities.LocationData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("location/share/{userId}")
    suspend fun shareLocation(
        @Path("userId") userId: String,
        @Body locationData: LocationData
    ): Response<Unit>

    fun getAvailableUsers()
}

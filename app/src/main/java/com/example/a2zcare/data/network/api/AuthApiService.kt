package com.example.a2zcare.data.network.api

import com.example.a2zcare.data.network.request.SignUpRequest
import com.example.a2zcare.data.network.response.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/Users/Register")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest
    ): Response<SignUpResponse>

}
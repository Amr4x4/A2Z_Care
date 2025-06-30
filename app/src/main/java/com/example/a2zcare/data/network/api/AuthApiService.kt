package com.example.a2zcare.data.network.api

import com.example.a2zcare.data.network.request.LoginRequest
import com.example.a2zcare.data.network.request.SignUpRequest
import com.example.a2zcare.data.network.response.ForgotPasswordResponse
import com.example.a2zcare.data.network.response.LoginResultResponse
import com.example.a2zcare.data.network.response.SignUpResultResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiService {

    @POST("api/Users/Register")
    suspend fun signUp(
        @Body request: SignUpRequest
    ): Response<SignUpResultResponse>

    @POST("api/Users/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResultResponse>

    @POST("api/Users/ForgotPassword")
    suspend fun forgetPassword(
        @Query("email") email: String
    ): Response<ForgotPasswordResponse>

}
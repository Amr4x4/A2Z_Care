package com.example.a2zcare.data.repository


import com.example.a2zcare.data.network.ApiService
import com.example.a2zcare.data.network.SignUpRequest
import com.example.a2zcare.domain.entities.SignUpResult
import com.example.a2zcare.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val api: ApiService): AuthRepository {
    override suspend fun signUp(userName: String, email: String, password: String): Result<SignUpResult> {
        return try {
            val response = api.signUp(SignUpRequest(userName, password, email))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                Result.success(SignUpResult(body.success, body.message, body.token))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
package com.example.a2zcare.data.repository

import com.example.a2zcare.data.network.api.AuthApiService
import com.example.a2zcare.data.network.request.SignUpRequest
import com.example.a2zcare.data.network.response.SignUpResponse
import com.example.a2zcare.domain.repository.AuthRepository
import com.example.a2zcare.domain.model.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRepository {

    override suspend fun signUp(
        userName: String,
        email: String,
        password: String,
        role: Int
    ): NetworkResult<SignUpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val signUpRequest = SignUpRequest(
                    userName = userName,
                    password = password,
                    email = email,
                    role = role
                )

                val response = authApiService.signUp(signUpRequest)

                if (response.isSuccessful) {
                    response.body()?.let { signUpResponse ->
                        // Check if the response indicates success
                        when (signUpResponse.status) {
                            200, 201 -> NetworkResult.Success(signUpResponse)
                            else -> NetworkResult.Error(
                                signUpResponse.detail ?: "Sign up failed with status: ${signUpResponse.status}"
                            )
                        }
                    } ?: NetworkResult.Error("Empty response body")
                } else {
                    NetworkResult.Error("HTTP ${response.code()}: ${response.message()}")
                }
            } catch (e: HttpException) {
                NetworkResult.Error("Network error: ${e.localizedMessage}")
            } catch (e: IOException) {
                NetworkResult.Error("Connection error: ${e.localizedMessage}")
            } catch (e: Exception) {
                NetworkResult.Error("Unexpected error: ${e.localizedMessage}")
            }
        }
    }
}
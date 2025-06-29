package com.example.a2zcare.data.repository

import android.util.Log
import com.example.a2zcare.data.network.api.AuthApiService
import com.example.a2zcare.data.network.request.LoginRequest
import com.example.a2zcare.data.network.request.SignUpRequest
import com.example.a2zcare.data.network.response.*
import com.example.a2zcare.domain.model.NetworkResult
import com.example.a2zcare.domain.repository.AuthRepository
import com.google.gson.Gson
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
    ): NetworkResult<SignUpResultResponse> = withContext(Dispatchers.IO) {
        try {
            val request = SignUpRequest(userName, email, password, role)
            val response = authApiService.signUp(request)

            Log.d("AuthRepository", "SignUp response: ${response.code()} ${response.message()}")

            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    return@withContext if (apiResponse.isSuccess()) {
                        NetworkResult.Success(apiResponse.result!!)
                    } else {
                        NetworkResult.Error(apiResponse.getErrorMessage())
                    }
                } ?: NetworkResult.Error("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "SignUp error body: $errorBody")

                val gson = Gson()
                val errorMessage = try {
                    val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                    errorResponse.getReadableError()
                } catch (e: Exception) {
                    "HTTP ${response.code()}: ${response.message()}"
                }

                NetworkResult.Error(errorMessage)
            }
        } catch (e: HttpException) {
            Log.e("AuthRepository", "HttpException: ${e.localizedMessage}", e)
            NetworkResult.Error("Network error: ${e.localizedMessage}")
        } catch (e: IOException) {
            Log.e("AuthRepository", "IOException: ${e.localizedMessage}", e)
            NetworkResult.Error("Connection error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Exception: ${e.localizedMessage}", e)
            NetworkResult.Error("Unexpected error: ${e.localizedMessage}")
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): NetworkResult<LoginResultResponse> = withContext(Dispatchers.IO) {
        try {
            val request = LoginRequest(email = email, password = password)
            val response = authApiService.login(request)

            Log.d("AuthRepository", "Login response: ${response.code()} ${response.message()}")

            if (response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    return@withContext if (apiResponse.isSuccess()) {
                        NetworkResult.Success(apiResponse.result!!)
                    } else {
                        NetworkResult.Error(apiResponse.getErrorMessage())
                    }
                } ?: NetworkResult.Error("Empty response body")
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("AuthRepository", "Login error body: $errorBody")

                val gson = Gson()
                val errorMessage = try {
                    val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                    errorResponse.getReadableError()
                } catch (e: Exception) {
                    "HTTP ${response.code()}: ${response.message()}"
                }

                NetworkResult.Error(errorMessage)
            }
        } catch (e: HttpException) {
            Log.e("AuthRepository", "HttpException: ${e.localizedMessage}", e)
            NetworkResult.Error("Network error: ${e.localizedMessage}")
        } catch (e: IOException) {
            Log.e("AuthRepository", "IOException: ${e.localizedMessage}", e)
            NetworkResult.Error("Connection error: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Exception: ${e.localizedMessage}", e)
            NetworkResult.Error("Unexpected error: ${e.localizedMessage}")
        }
    }
}

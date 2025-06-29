package com.example.a2zcare.data.repository

import android.util.Log
import com.example.a2zcare.data.network.api.AuthApiService
import com.example.a2zcare.data.network.request.LoginRequest
import com.example.a2zcare.data.network.request.SignUpRequest
import com.example.a2zcare.data.network.response.ApiErrorResponse
import com.example.a2zcare.data.network.response.LoginResultResponse
import com.example.a2zcare.data.network.response.SignUpResultResponse
import com.example.a2zcare.domain.model.NetworkResult
import com.example.a2zcare.domain.repository.AuthRepository
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
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

    private val gson = Gson()

    override suspend fun signUp(
        userName: String,
        email: String,
        password: String,
        role: Int
    ): NetworkResult<SignUpResultResponse> = withContext(Dispatchers.IO) {
        try {
            val request = SignUpRequest(
                userName = userName,
                password = password,
                email = email,
                role = role
            )

            Log.d("AuthRepository", "SignUp request: ${gson.toJson(request)}")

            val response = authApiService.signUp(request)

            Log.d("AuthRepository", "SignUp response code: ${response.code()}")
            Log.d("AuthRepository", "SignUp response message: ${response.message()}")

            // Log the raw response body for debugging
            val rawResponse = response.raw().toString()
            Log.d("AuthRepository", "SignUp raw response: $rawResponse")

            when (response.code()) {
                200, 201 -> {
                    // Success response
                    response.body()?.let { signUpResult ->
                        Log.d("AuthRepository", "SignUp success: ${gson.toJson(signUpResult)}")
                        NetworkResult.Success(signUpResult)
                    } ?: run {
                        Log.e("AuthRepository", "SignUp success but empty body")
                        NetworkResult.Error("Registration completed but no response data received")
                    }
                }
                400 -> {
                    // Bad Request - usually validation errors
                    val errorBody = response.errorBody()?.string()
                    Log.e("AuthRepository", "SignUp 400 error body: $errorBody")

                    val errorMessage = parseErrorResponse(errorBody) ?: "Invalid registration data"
                    NetworkResult.Error(errorMessage)
                }
                409 -> {
                    // Conflict - user already exists
                    val errorBody = response.errorBody()?.string()
                    Log.e("AuthRepository", "SignUp 409 error body: $errorBody")

                    val errorMessage = parseErrorResponse(errorBody) ?: "User already exists"
                    NetworkResult.Error(errorMessage)
                }
                else -> {
                    // Other HTTP errors
                    val errorBody = response.errorBody()?.string()
                    Log.e("AuthRepository", "SignUp ${response.code()} error body: $errorBody")

                    val errorMessage = parseErrorResponse(errorBody)
                        ?: "Registration failed with code ${response.code()}"
                    NetworkResult.Error(errorMessage)
                }
            }
        } catch (e: HttpException) {
            Log.e("AuthRepository", "SignUp HttpException: ${e.code()} - ${e.message()}", e)

            val errorBody = e.response()?.errorBody()?.string()
            Log.e("AuthRepository", "SignUp HttpException error body: $errorBody")

            val errorMessage = parseErrorResponse(errorBody) ?: "Network error: ${e.message()}"
            NetworkResult.Error(errorMessage)
        } catch (e: IOException) {
            Log.e("AuthRepository", "SignUp IOException: ${e.message}", e)
            NetworkResult.Error("Connection error: Please check your internet connection")
        } catch (e: Exception) {
            Log.e("AuthRepository", "SignUp Exception: ${e.message}", e)
            NetworkResult.Error("Unexpected error: ${e.message}")
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): NetworkResult<LoginResultResponse> = withContext(Dispatchers.IO) {
        try {
            val request = if (email.contains("@")) {
                LoginRequest(email = email, password = password, username = null)
            } else {
                LoginRequest(email = null, password = password, username = email)
            }

            Log.d("AuthRepository", "Login request: ${gson.toJson(request)}")

            val response = authApiService.login(request)

            Log.d("AuthRepository", "Login response code: ${response.code()}")
            Log.d("AuthRepository", "Login response message: ${response.message()}")

            when (response.code()) {
                200 -> {
                    response.body()?.let { loginResult ->
                        Log.d("AuthRepository", "Login success: ${gson.toJson(loginResult)}")
                        NetworkResult.Success(loginResult)
                    } ?: run {
                        Log.e("AuthRepository", "Login success but empty body")
                        NetworkResult.Error("Login completed but no response data received")
                    }
                }
                401 -> {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AuthRepository", "Login 401 error body: $errorBody")

                    val errorMessage = parseErrorResponse(errorBody) ?: "Invalid credentials"
                    NetworkResult.Error(errorMessage)
                }
                404 -> {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AuthRepository", "Login 404 error body: $errorBody")

                    val errorMessage = parseErrorResponse(errorBody) ?: "User not found"
                    NetworkResult.Error(errorMessage)
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AuthRepository", "Login ${response.code()} error body: $errorBody")

                    val errorMessage = parseErrorResponse(errorBody)
                        ?: "Login failed with code ${response.code()}"
                    NetworkResult.Error(errorMessage)
                }
            }
        } catch (e: HttpException) {
            Log.e("AuthRepository", "Login HttpException: ${e.code()} - ${e.message()}", e)

            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = parseErrorResponse(errorBody) ?: "Network error: ${e.message()}"
            NetworkResult.Error(errorMessage)
        } catch (e: IOException) {
            Log.e("AuthRepository", "Login IOException: ${e.message}", e)
            NetworkResult.Error("Connection error: Please check your internet connection")
        } catch (e: Exception) {
            Log.e("AuthRepository", "Login Exception: ${e.message}", e)
            NetworkResult.Error("Unexpected error: ${e.message}")
        }
    }

    private fun parseErrorResponse(errorBody: String?): String? {
        if (errorBody.isNullOrBlank()) return null

        return try {
            // Try to parse as ApiErrorResponse first
            val errorResponse = gson.fromJson(errorBody, ApiErrorResponse::class.java)
            errorResponse.getReadableError()
        } catch (e: JsonSyntaxException) {
            try {
                // Try to parse as a simple error object with different structure
                val errorMap = gson.fromJson(errorBody, Map::class.java)
                when {
                    errorMap.containsKey("message") -> errorMap["message"]?.toString()
                    errorMap.containsKey("error") -> errorMap["error"]?.toString()
                    errorMap.containsKey("detail") -> errorMap["detail"]?.toString()
                    errorMap.containsKey("title") -> errorMap["title"]?.toString()
                    else -> null
                }
            } catch (e2: Exception) {
                // If all parsing fails, return the raw error body (truncated)
                errorBody.take(200) // Limit to 200 characters
            }
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error parsing error response", e)
            null
        }
    }
}

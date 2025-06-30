package com.example.a2zcare.data.repository

import android.util.Log
import com.example.a2zcare.data.model.User
import com.example.a2zcare.data.network.api.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getUserByUsername(username: String): Result<User> {
        return try {
            val trimmedUsername = username.trim()
            Log.d("UserRepository", "API Call - Fetching user: '$trimmedUsername'")

            val response = apiService.getUserByUsername(trimmedUsername)

            Log.d("UserRepository", "API Response - Code: ${response.code()}")
            Log.d("UserRepository", "API Response - Success: ${response.isSuccessful}")
            Log.d("UserRepository", "API Response - Body: ${response.body()}")

            if (response.isSuccessful) {
                val userResponse = response.body()
                Log.d("UserRepository", "UserResponse - isSuccess: ${userResponse?.isSuccess}")
                Log.d("UserRepository", "UserResponse - result: ${userResponse?.result}")
                Log.d("UserRepository", "UserResponse - errors: ${userResponse?.errors}")

                if (userResponse?.isSuccess == true && userResponse.result != null) {
                    val user = userResponse.result
                    Log.d("UserRepository", "SUCCESS - User found: '${user.userName}', Email: '${user.email}', Role: '${user.role}'")
                    Result.success(user)
                } else {
                    val errorMsg = "API returned isSuccess=false: ${userResponse?.errors?.joinToString() ?: "Unknown error"}"
                    Log.e("UserRepository", errorMsg)
                    Result.failure(Exception(errorMsg))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMsg = "HTTP Error ${response.code()}: ${response.message()}, Body: $errorBody"
                Log.e("UserRepository", errorMsg)
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Exception fetching user '$username': ${e.message}", e)
            Result.failure(e)
        }
    }
}

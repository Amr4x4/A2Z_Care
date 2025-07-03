package com.example.a2zcare.di

import com.example.a2zcare.data.remote.response.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val url = originalRequest.url.toString()

        // Allow unauthenticated access to login/register
        if (url.contains("/Login", ignoreCase = true) || url.contains("/Register", ignoreCase = true)) {
            return chain.proceed(originalRequest)
        }

        // Safely get token in a blocking manner
        val token = runBlocking { tokenManager.getToken() }

        val authenticatedRequest = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(authenticatedRequest)
    }
}

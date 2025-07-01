package com.example.a2zcare.di

import com.example.a2zcare.data.remote.response.TokenManager
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
        if (url.contains("/Login") || url.contains("/Register")) {
            return chain.proceed(originalRequest)
        }

        val token = tokenManager.getToken()

        val authenticatedRequest = if (token != null) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(authenticatedRequest)
    }
}
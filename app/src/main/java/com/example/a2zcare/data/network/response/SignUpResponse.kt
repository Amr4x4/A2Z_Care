package com.example.a2zcare.data.network.response

import com.google.gson.annotations.SerializedName

data class SignUpResultResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("userName") val userName: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("success") val success: Boolean? = null
)

fun SignUpResultResponse.getWelcomeMessage(): String {
    return when {
        !message.isNullOrBlank() -> message
        !userName.isNullOrBlank() -> "Welcome $userName! Registration successful!"
        else -> "Registration successful! Please login to continue."
    }
}
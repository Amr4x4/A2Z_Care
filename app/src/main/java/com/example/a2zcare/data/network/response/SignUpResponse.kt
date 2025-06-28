package com.example.a2zcare.data.network.response

import com.google.gson.annotations.SerializedName

data class SignUpResultResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("userName") val userName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("role") val role: String?
)

fun SignUpResultResponse.getWelcomeMessage(): String {
    return if (!userName.isNullOrEmpty()) {
        "Welcome $userName! Registration successful!"
    } else {
        "Registration successful! Please login to continue."
    }
}

fun ApiBaseResponse<SignUpResultResponse>.getErrorMessage(): String {
    return errors?.joinToString() ?: "Unknown error occurred"
}

fun ApiBaseResponse<SignUpResultResponse>.isSuccess(): Boolean = isSuccess == true && result != null

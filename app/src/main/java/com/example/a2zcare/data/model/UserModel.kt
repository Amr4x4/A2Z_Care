package com.example.a2zcare.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: String,
    @SerializedName("userName") val userName: String,
    @SerializedName("email") val email: String,
    @SerializedName("role") val role: String,
    @SerializedName("name") val name: String? = null,
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("dateOfBirth") val dateOfBirth: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("weightKg") val weightKg: Double? = null,
    @SerializedName("height") val height: Double? = null,
    @SerializedName("healthGoals") val healthGoals: String? = null,
    @SerializedName("createdDate") val createdDate: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)

/*
data class RegisterRequest(
    val userName: String,
    val password: String,
    val email: String,
    val role: Int = 0
)

data class LoginRequest(
    val email: String,
    val password: String,
    val username: String
)

data class LogoutRequest(
    val userId: String,
    val token: String,
    val deviceId: String
)

data class UpdateUserRequest(
    val id: String,
    val firstName: String,
    val lastName: String,
    val name: String,
    val phoneNumber: String,
    val address: String,
    val age: Int,
    val dateOfBirth: String,
    val gender: String,
    val weightKg: Double,
    val height: Double,
    val healthGoals: String,
    val updatedAt: String
)

data class ResetPasswordRequest(
    val email: String,
    val currentPassword: String,
    val newPassword: String
)

data class ResetForgotPasswordRequest(
    val userId: String,
    val token: String,
    val newPassword: String
)

data class SendEmailRequest(
    val toEmail: String,
    val subject: String,
    val body: String,
    val attachments: List<String>?
)
 */
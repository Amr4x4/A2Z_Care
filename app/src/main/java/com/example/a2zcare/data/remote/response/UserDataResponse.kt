package com.example.a2zcare.data.remote.response

data class UserDataResponse(
    val id: String,
    val userName: String,
    val email: String,
    val role: String,
    val name: String?,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val address: String?,
    val dateOfBirth: String?,
    val gender: String?,
    val age: Int,
    val weightKg: Double,
    val height: Double,
    val healthGoals: String?,
    val createdDate: String,
    val updatedAt: String?
)
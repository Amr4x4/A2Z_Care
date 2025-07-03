package com.example.a2zcare.data.remote.response

data class UpdateUserResponse(
    val id: String,
    val firstName: String?,
    val lastName: String?,
    val name: String?,
    val phoneNumber: String?,
    val address: String?,
    val age: Int,
    val dateOfBirth: String?,
    val gender: String?,
    val weightKg: Double,
    val height: Double,
    val healthGoals: String?,
    val updatedAt: String?
)
package com.example.a2zcare.data.remote.request

import com.google.gson.annotations.SerializedName

data class UpdateUserRequest(
    @SerializedName("id") val id: String,
    @SerializedName("firstName") val firstName: String? = null,
    @SerializedName("lastName") val lastName: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("dateOfBirth") val dateOfBirth: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("weightKg") val weightKg: Double? = null,
    @SerializedName("height") val height: Int? = null, // Changed from Double? to Int?
    @SerializedName("healthGoals") val healthGoals: String? = null,
    @SerializedName("updatedAt") val updatedAt: String? = null
)
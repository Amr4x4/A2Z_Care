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

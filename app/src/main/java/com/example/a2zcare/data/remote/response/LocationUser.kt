package com.example.a2zcare.data.remote.response

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

// Fix 7: Update LocationUser response model
data class LocationUser(
    val id: String,
    val userName: String,
    val email: String,
    val name: String
)
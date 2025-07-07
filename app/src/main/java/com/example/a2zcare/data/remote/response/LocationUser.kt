package com.example.a2zcare.data.remote.response

data class LocationUser(
    val id: String,
    val userName: String,
    val email: String
) {
    // Add a computed property for backward compatibility with UI that expects 'name'
    val name: String get() = userName
}

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
package com.example.a2zcare.data.model

data class ShareLocationRequest(
    val fromUserId: String,
    val toUserId: String,
    val latitude: Double,
    val longitude: Double,
    val address: String? = null,
    val message: String? = null
)
package com.example.a2zcare.data.network.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String? = null
)
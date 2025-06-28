package com.example.a2zcare.data.network.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("UserName")
    val userName: String,
    @SerializedName("Password")
    val password: String,
    @SerializedName("Email")
    val email: String,
    @SerializedName("Role")
    val role: Int = 0
)

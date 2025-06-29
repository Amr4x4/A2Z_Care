package com.example.a2zcare.data.network.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("userName")
    val userName: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("role")
    val role: Int = 0
)
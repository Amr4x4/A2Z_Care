package com.example.a2zcare.data.remote.response

import com.example.a2zcare.data.model.User
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user") val user: User,
    @SerializedName("token") val token: String
)
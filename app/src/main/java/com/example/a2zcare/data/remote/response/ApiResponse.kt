package com.example.a2zcare.data.remote.response

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("errors") val errors: List<String>?,
    @SerializedName("result") val result: T?
)
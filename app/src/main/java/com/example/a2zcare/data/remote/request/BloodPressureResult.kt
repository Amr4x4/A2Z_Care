package com.example.a2zcare.data.remote.request

import com.google.gson.annotations.SerializedName

data class BloodPressureResult(
    @SerializedName("systolic") val systolic: Double,
    @SerializedName("diastolic") val diastolic: Double,
    @SerializedName("category") val category: String
)
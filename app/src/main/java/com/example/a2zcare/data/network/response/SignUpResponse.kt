package com.example.a2zcare.data.network.response

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("type")
    val type: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("instance")
    val instance: String?,
    @SerializedName("additionalProp1")
    val additionalProp1: String?,
    @SerializedName("additionalProp2")
    val additionalProp2: String?,
    @SerializedName("additionalProp3")
    val additionalProp3: String?
)
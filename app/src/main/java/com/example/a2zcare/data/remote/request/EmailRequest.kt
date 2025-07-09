package com.example.a2zcare.data.remote.request

import com.google.gson.annotations.SerializedName

data class EmailRequest(
    @SerializedName("ToEmail") val toEmail: String,
    @SerializedName("Subject") val subject: String,
    @SerializedName("Body") val body: String,
    @SerializedName("attachments") val attachments: List<String>? = null // Fixed: Capitalized "Attachments"
)
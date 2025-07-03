package com.example.a2zcare.data.remote.request

import com.google.gson.annotations.SerializedName

data class SendEmailRequest(
    @SerializedName("toEmail") val toEmail: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("body") val body: String,
    @SerializedName("attachments") val attachments: List<String>? = null
)
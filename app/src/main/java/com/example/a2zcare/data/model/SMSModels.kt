package com.example.a2zcare.data.model

data class SendSMSRequest(
    val phoneNumber: String,
    val body: String
)

data class SendMessageRequest(
    val phoneNumber: String,
    val message: String
)


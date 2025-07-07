package com.example.a2zcare.data.remote.response

data class EmailResponse(
    val toEmail: String,
    val subject: String,
    val body: String,
    val attachments: List<String>?
)
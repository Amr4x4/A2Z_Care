package com.example.a2zcare.data.remote.request

data class EmailRequest(
    val toEmail: String,
    val subject: String,
    val body: String,
    val attachments: List<String>? = null
)
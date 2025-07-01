package com.example.a2zcare.data.model

data class ErrorResponse(
    val type: String?,
    val title: String?,
    val status: Int,
    val detail: String?,
    val instance: String?,
    val additionalProp1: String?,
    val additionalProp2: String?,
    val additionalProp3: String?
)
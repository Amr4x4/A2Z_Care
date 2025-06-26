package com.example.a2zcare.domain.entities

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profileImageUrl: String? = null
)
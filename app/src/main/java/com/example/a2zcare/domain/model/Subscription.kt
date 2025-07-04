package com.example.a2zcare.domain.model

data class Subscription(
    val id: String,
    val name: String,
    val price: Double,
    val duration: String,
    val features: List<String>,
    val isActive: Boolean = false,
    val discountPercentage: Int = 0
)
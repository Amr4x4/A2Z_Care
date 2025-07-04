package com.example.a2zcare.domain.model

data class PaymentMethod(
    val id: String,
    val type: PaymentType,
    val displayName: String,
    val email: String? = null,
    val lastFourDigits: String? = null,
    val isLinked: Boolean = false
)

enum class PaymentType {
    PAYPAL, GOOGLE_PAY, APPLE_PAY, MASTERCARD, VISA, AMEX
}
package com.example.a2zcare.domain.model

data class SecuritySettings(
    val biometricEnabled: Boolean = false,
    val faceIdEnabled: Boolean = false,
    val smsAuthEnabled: Boolean = false,
    val googleAuthEnabled: Boolean = false
)
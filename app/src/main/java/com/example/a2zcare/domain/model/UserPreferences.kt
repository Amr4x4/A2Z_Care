package com.example.a2zcare.domain.model

data class UserPreferences(
    val weightUnit: String = "kg",
    val heightUnit: String = "cm",
    val bloodSugarUnit: String = "mg/dL",
    val firstDayOfWeek: String = "Monday",
    val timeFormat: String = "System Default",
    val dayResetTime: String = "00:00 AM",
    val reminderEnabled: Boolean = false,
    val reminderTime: String = "09:00 AM",
    val reminderRingtone: String = "Lollipop",
    val vibrationEnabled: Boolean = false,
    val theme: AppTheme = AppTheme.SYSTEM_DEFAULT,
    val language: String = "English"
)

enum class AppTheme {
    LIGHT, DARK, SYSTEM_DEFAULT
}
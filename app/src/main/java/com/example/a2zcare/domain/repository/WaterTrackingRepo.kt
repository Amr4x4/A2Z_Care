package com.example.a2zcare.domain.repository

interface WaterTrackingRepo {
    suspend fun getGlassCount(): Int
    suspend fun incrementGlassCount()
    suspend fun getReminderInterval(): Int
    suspend fun setReminderInterval(minute: Int)
}
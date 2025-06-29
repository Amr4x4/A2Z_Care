package com.example.a2zcare.domain.repository

import com.example.a2zcare.domain.entities.LocationNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    fun getNotifications(): Flow<List<LocationNotification>>
    suspend fun addNotification(notification: LocationNotification)
    suspend fun markAsRead(id: String)
    suspend fun deleteNotification(id: String)
}

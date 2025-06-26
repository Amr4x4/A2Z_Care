package com.example.a2zcare.domain.repository

import com.example.a2zcare.domain.entities.LocationNotification
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun getNotifications(): Flow<List<LocationNotification>>
    suspend fun markAsRead(notificationId: String)
    suspend fun deleteNotification(notificationId: String)
}
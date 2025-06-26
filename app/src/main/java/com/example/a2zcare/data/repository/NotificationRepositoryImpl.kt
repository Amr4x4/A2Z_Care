package com.example.a2zcare.data.repository

import com.example.a2zcare.domain.entities.LocationData
import com.example.a2zcare.domain.entities.LocationNotification
import com.example.a2zcare.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepositoryImpl @Inject constructor() : NotificationRepository {

    private val _notifications = MutableStateFlow<List<LocationNotification>>(
        // Mock notifications for demo
        listOf(
            LocationNotification(
                id = "1",
                fromUserId = "user1",
                fromUserName = "John Doe",
                location = LocationData(37.7749, -122.4194, System.currentTimeMillis()),
                message = "I'm at Golden Gate Park for a morning run!",
                timestamp = Date(System.currentTimeMillis() - 3600000), // 1 hour ago
                isRead = false
            ),
            LocationNotification(
                id = "2",
                fromUserId = "user2",
                fromUserName = "Jane Smith",
                location = LocationData(37.7849, -122.4094, System.currentTimeMillis()),
                message = "Just finished my workout at the gym.",
                timestamp = Date(System.currentTimeMillis() - 7200000), // 2 hours ago
                isRead = true
            )
        )
    )

    override suspend fun getNotifications(): Flow<List<LocationNotification>> {
        return _notifications.asStateFlow()
    }

    override suspend fun markAsRead(notificationId: String) {
        _notifications.value = _notifications.value.map { notification ->
            if (notification.id == notificationId) {
                notification.copy(isRead = true)
            } else {
                notification
            }
        }
    }

    override suspend fun deleteNotification(notificationId: String) {
        _notifications.value = _notifications.value.filter { it.id != notificationId }
    }

    // Method to add new notifications (for simulation or real implementation)
    fun addNotification(notification: LocationNotification) {
        _notifications.value = _notifications.value + notification
    }
}
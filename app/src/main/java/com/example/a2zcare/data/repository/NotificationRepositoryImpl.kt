package com.example.a2zcare.data.repository

import com.example.a2zcare.domain.entities.LocationNotification
import com.example.a2zcare.domain.repository.NotificationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NotificationRepositoryImpl @Inject constructor() : NotificationRepository {

    private val notifications = mutableListOf<LocationNotification>()
    private val notificationsFlow = MutableStateFlow<List<LocationNotification>>(emptyList())

    override fun getNotifications(): Flow<List<LocationNotification>> {
        return notificationsFlow.asStateFlow()
    }

    override suspend fun addNotification(notification: LocationNotification) {
        notifications.add(0, notification) // newest first
        notificationsFlow.value = notifications.toList()
    }

    override suspend fun markAsRead(id: String) {
        notifications.replaceAll {
            if (it.id == id) it.copy(isRead = true) else it
        }
        notificationsFlow.value = notifications.toList()
    }

    override suspend fun deleteNotification(id: String) {
        notifications.removeAll { it.id == id }
        notificationsFlow.value = notifications.toList()
    }
}

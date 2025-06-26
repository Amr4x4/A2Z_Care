package com.example.a2zcare.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.domain.entities.LocationData
import com.example.a2zcare.domain.entities.LocationNotification
import com.example.a2zcare.domain.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

data class NotificationUiState(
    val notifications: List<LocationNotification> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    init {
        loadNotifications()
        // Simulate receiving notifications
        simulateNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            notificationRepository.getNotifications().collect { notifications ->
                _uiState.value = _uiState.value.copy(
                    notifications = notifications,
                    isLoading = false
                )
            }
        }
    }

    private fun simulateNotifications() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(5000)
            val mockNotification = LocationNotification(
                id = "1",
                fromUserId = "user123",
                fromUserName = "John Doe",
                location = LocationData(37.7749, -122.4194, System.currentTimeMillis()),
                message = "I'm at Golden Gate Park!",
                timestamp = Date()
            )
            // Add to notifications (implement with proper repository)
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.markAsRead(notificationId)
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            notificationRepository.deleteNotification(notificationId)
        }
    }
}

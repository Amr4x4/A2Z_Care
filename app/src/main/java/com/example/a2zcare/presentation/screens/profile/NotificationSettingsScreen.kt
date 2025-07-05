package com.example.a2zcare.presentation.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.a2zcare.presentation.common_ui.MiniTopBar
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.viewmodel.ProfileViewModel

@Composable
fun NotificationSettingsScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userPreferences by viewModel.userPreferences.collectAsStateWithLifecycle()

    // Local state for notification settings
    var pushNotifications by remember { mutableStateOf(true) }
    var emailNotifications by remember { mutableStateOf(true) }
    var smsNotifications by remember { mutableStateOf(false) }
    var appointmentReminders by remember { mutableStateOf(true) }
    var medicationReminders by remember { mutableStateOf(true) }
    var healthTips by remember { mutableStateOf(true) }
    var promotionalEmails by remember { mutableStateOf(false) }
    var weeklyReports by remember { mutableStateOf(true) }
    var emergencyAlerts by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            MiniTopBar(
                title = "Notification Settings",
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(backgroundColor)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // General Notifications
                        NotificationSection(
                            title = "General Notifications",
                            icon = Icons.Default.Notifications,
                            items = listOf(
                                NotificationItem(
                                    title = "Push Notifications",
                                    description = "Receive push notifications on your device",
                                    isEnabled = pushNotifications,
                                    onToggle = { pushNotifications = it }
                                ),
                                NotificationItem(
                                    title = "Email Notifications",
                                    description = "Receive notifications via email",
                                    isEnabled = emailNotifications,
                                    onToggle = { emailNotifications = it }
                                ),
                                NotificationItem(
                                    title = "SMS Notifications",
                                    description = "Receive notifications via SMS",
                                    isEnabled = smsNotifications,
                                    onToggle = { smsNotifications = it }
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Health & Medical
                        NotificationSection(
                            title = "Health & Medical",
                            icon = Icons.Default.MedicalServices,
                            items = listOf(
                                NotificationItem(
                                    title = "Appointment Reminders",
                                    description = "Get reminded about upcoming appointments",
                                    isEnabled = appointmentReminders,
                                    onToggle = { appointmentReminders = it }
                                ),
                                NotificationItem(
                                    title = "Medication Reminders",
                                    description = "Never miss your medication schedule",
                                    isEnabled = medicationReminders,
                                    onToggle = { medicationReminders = it }
                                ),
                                NotificationItem(
                                    title = "Health Tips",
                                    description = "Receive daily health tips and advice",
                                    isEnabled = healthTips,
                                    onToggle = { healthTips = it }
                                ),
                                NotificationItem(
                                    title = "Emergency Alerts",
                                    description = "Critical health alerts and warnings",
                                    isEnabled = emergencyAlerts,
                                    onToggle = { emergencyAlerts = it }
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Marketing & Updates
                        NotificationSection(
                            title = "Marketing & Updates",
                            icon = Icons.Default.Campaign,
                            items = listOf(
                                NotificationItem(
                                    title = "Promotional Emails",
                                    description = "Receive offers and promotional content",
                                    isEnabled = promotionalEmails,
                                    onToggle = { promotionalEmails = it }
                                ),
                                NotificationItem(
                                    title = "Weekly Reports",
                                    description = "Get weekly health and activity reports",
                                    isEnabled = weeklyReports,
                                    onToggle = { weeklyReports = it }
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Save Button
                        Button(
                            onClick = {
                                // Save notification preferences
                                // viewModel.updateNotificationPreferences(...)
                                navController.popBackStack()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Save Settings",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationSection(
    title: String,
    icon: ImageVector,
    items: List<NotificationItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = fieldCardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1976D2)
                )
            }

            items.forEachIndexed { index, item ->
                NotificationItemRow(
                    item = item,
                    showDivider = index < items.size - 1
                )
            }
        }
    }
}

@Composable
private fun NotificationItemRow(
    item: NotificationItem,
    showDivider: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = item.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Switch(
            checked = item.isEnabled,
            onCheckedChange = item.onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF1976D2),
                checkedTrackColor = Color(0xFF1976D2).copy(alpha = 0.5f)
            )
        )
    }

    if (showDivider) {
        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

private data class NotificationItem(
    val title: String,
    val description: String,
    val isEnabled: Boolean,
    val onToggle: (Boolean) -> Unit
)
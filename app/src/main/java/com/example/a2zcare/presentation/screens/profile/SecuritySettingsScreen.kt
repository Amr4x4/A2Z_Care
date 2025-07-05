package com.example.a2zcare.presentation.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.viewmodel.ProfileViewModel

@Composable
fun SecuritySettingsScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val securitySettings by viewModel.securitySettings.collectAsStateWithLifecycle()
    // Security options
    var twoFactorEnabled by remember { mutableStateOf(false) }
    var biometricEnabled by remember { mutableStateOf(false) }
    var loginAlerts by remember { mutableStateOf(true) }
    var deviceTracking by remember { mutableStateOf(true) }
    var autoLogout by remember { mutableStateOf(true) }

    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showTwoFactorDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            MiniTopBar(
                title = "Security Settings",
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
                        // Password & Authentication
                        SecuritySection(
                            title = "Password & Authentication",
                            icon = Icons.Default.Lock,
                            items = listOf(
                                SecurityItem(
                                    title = "Change Password",
                                    description = "Update your account password",
                                    icon = Icons.Default.Key,
                                    onClick = { navController.navigate(Screen.ChangePassword.route) }
                                ),
                                SecurityItem(
                                    title = "Two-Factor Authentication",
                                    description = if (twoFactorEnabled) "Enabled" else "Disabled",
                                    icon = Icons.Default.Security,
                                    onClick = { showTwoFactorDialog = true },
                                    hasSwitch = true,
                                    isEnabled = twoFactorEnabled,
                                    onToggle = { twoFactorEnabled = it }
                                ),
                                SecurityItem(
                                    title = "Biometric Login",
                                    description = "Use fingerprint or face recognition",
                                    icon = Icons.Default.Fingerprint,
                                    hasSwitch = true,
                                    isEnabled = biometricEnabled,
                                    onToggle = { biometricEnabled = it }
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Account Security
                        SecuritySection(
                            title = "Account Security",
                            icon = Icons.Default.Shield,
                            items = listOf(
                                SecurityItem(
                                    title = "Login Alerts",
                                    description = "Get notified of new device logins",
                                    icon = Icons.Default.NotificationsActive,
                                    hasSwitch = true,
                                    isEnabled = loginAlerts,
                                    onToggle = { loginAlerts = it }
                                ),
                                SecurityItem(
                                    title = "Device Tracking",
                                    description = "Monitor devices accessing your account",
                                    icon = Icons.Default.Devices,
                                    hasSwitch = true,
                                    isEnabled = deviceTracking,
                                    onToggle = { deviceTracking = it }
                                ),
                                SecurityItem(
                                    title = "Auto Logout",
                                    description = "Automatically logout after inactivity",
                                    icon = Icons.Default.Timer,
                                    hasSwitch = true,
                                    isEnabled = autoLogout,
                                    onToggle = { autoLogout = it }
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Privacy & Data
                        SecuritySection(
                            title = "Privacy & Data",
                            icon = Icons.Default.PrivacyTip,
                            items = listOf(
                                SecurityItem(
                                    title = "Active Sessions",
                                    description = "Manage active device sessions",
                                    icon = Icons.Default.DeviceHub,
                                    onClick = { /* Navigate to active sessions */ }
                                ),
                                SecurityItem(
                                    title = "Data Export",
                                    description = "Download your personal data",
                                    icon = Icons.Default.Download,
                                    onClick = { /* Initiate data export */ }
                                )
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Save Button
                        Button(
                            onClick = {
                                // Save security settings
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

    // Two-Factor Authentication Dialog
    if (showTwoFactorDialog) {
        AlertDialog(
            onDismissRequest = { showTwoFactorDialog = false },
            title = { Text("Two-Factor Authentication") },
            text = {
                Column {
                    Text("Two-factor authentication adds an extra layer of security to your account.")
                    Spacer(modifier = Modifier.height(16.dp))
                    if (twoFactorEnabled) {
                        Text("Two-factor authentication is currently enabled.")
                    } else {
                        Text("Enable two-factor authentication to secure your account with SMS or authenticator app.")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        twoFactorEnabled = !twoFactorEnabled
                        showTwoFactorDialog = false
                    }
                ) {
                    Text(if (twoFactorEnabled) "Disable" else "Enable")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTwoFactorDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SecuritySection(
    title: String,
    icon: ImageVector,
    items: List<SecurityItem>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
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
                SecurityItemRow(
                    item = item,
                    showDivider = index < items.size - 1
                )
            }
        }
    }
}

@Composable
private fun SecurityItemRow(
    item: SecurityItem,
    showDivider: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !item.hasSwitch) { item.onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(16.dp))

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

        if (item.hasSwitch) {
            Switch(
                checked = item.isEnabled,
                onCheckedChange = item.onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF1976D2),
                    checkedTrackColor = Color(0xFF1976D2).copy(alpha = 0.5f)
                )
            )
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }

    if (showDivider) {
        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

private data class SecurityItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit = {},
    val hasSwitch: Boolean = false,
    val isEnabled: Boolean = false,
    val onToggle: (Boolean) -> Unit = {}
)
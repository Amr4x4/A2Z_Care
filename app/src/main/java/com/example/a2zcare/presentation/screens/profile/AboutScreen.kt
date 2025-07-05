package com.example.a2zcare.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a2zcare.presentation.common_ui.MiniTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            MiniTopBar(
                title = "About",
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Logo and Name
            item {
                AppHeader()
            }

            // App Description
            item {
                AppDescription()
            }

            // App Features
            item {
                AppFeatures()
            }

            // Version Info
            item {
                VersionInfo()
            }

            // Developer Info
            item {
                DeveloperInfo()
            }

            // Legal Info
            item {
                LegalInfo()
            }

            // Contact Info
            item {
                ContactInfo()
            }
        }
    }
}

@Composable
private fun AppHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Icon/Logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(20.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = "A2Z Care Logo",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "A2Z Care",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Your Complete Healthcare Companion",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AppDescription() {
    InfoCard(
        title = "About A2Z Care",
        icon = Icons.Default.Info
    ) {
        Text(
            text = "A2Z Care is a comprehensive health monitoring application designed to help you track your physical activities, manage your daily health needs, and receive personalized health insights. Our app uses AI-powered predictions to monitor important health metrics and provide you with actionable recommendations for a healthier lifestyle.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun AppFeatures() {
    InfoCard(
        title = "Key Features",
        icon = Icons.Default.Star
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FeatureItem(
                icon = Icons.Default.FitnessCenter,
                title = "Activity Tracking",
                description = "Monitor your daily physical activities and fitness goals"
            )
            FeatureItem(
                icon = Icons.Default.Analytics,
                title = "Health Analytics",
                description = "AI-powered insights and personalized health recommendations"
            )
            FeatureItem(
                icon = Icons.Default.Notifications,
                title = "Smart Reminders",
                description = "Timely notifications for medications and health checkups"
            )
            FeatureItem(
                icon = Icons.Default.Security,
                title = "Data Security",
                description = "Your health data is encrypted and securely stored"
            )
            FeatureItem(
                icon = Icons.Default.CloudSync,
                title = "Cloud Sync",
                description = "Access your health data across all your devices"
            )
        }
    }
}

@Composable
private fun FeatureItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun VersionInfo() {
    InfoCard(
        title = "Version Information",
        icon = Icons.Default.Update
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoRow("Version", "1.0.0")
            InfoRow("Build", "100")
            InfoRow("Release Date", "January 2025")
            InfoRow("Platform", "Android")
            InfoRow("Minimum OS", "Android 7.0 (API 24)")
        }
    }
}

@Composable
private fun DeveloperInfo() {
    InfoCard(
        title = "Developer Information",
        icon = Icons.Default.Code
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoRow("Company", "A2Z Care Technologies")
            InfoRow("Website", "www.a2zcare.com")
            InfoRow("Email", "info@a2zcare.com")
            InfoRow("Support", "support@a2zcare.com")
        }
    }
}

@Composable
private fun LegalInfo() {
    InfoCard(
        title = "Legal Information",
        icon = Icons.Default.Gavel
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "© 2025 A2Z Care Technologies. All rights reserved.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "This app is licensed under the MIT License. Third-party libraries and their respective licenses are acknowledged in the app documentation.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun ContactInfo() {
    InfoCard(
        title = "Contact & Support",
        icon = Icons.Default.ContactSupport
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ContactItem(
                icon = Icons.Default.Email,
                title = "Email Support",
                value = "support@a2zcare.com"
            )
            ContactItem(
                icon = Icons.Default.Phone,
                title = "Phone Support",
                value = "+1-800-A2Z-CARE"
            )
            ContactItem(
                icon = Icons.Default.Language,
                title = "Website",
                value = "www.a2zcare.com"
            )
            ContactItem(
                icon = Icons.Default.Schedule,
                title = "Support Hours",
                value = "24/7 Available"
            )
        }
    }
}

@Composable
private fun ContactItem(
    icon: ImageVector,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            content()
        }
    }
}
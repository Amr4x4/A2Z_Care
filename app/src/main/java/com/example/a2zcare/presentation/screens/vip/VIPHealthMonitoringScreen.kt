package com.example.a2zcare.presentation.screens.vip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VIPHealthMonitoringScreen(navController: NavController) {
    var selectedPeriod by remember { mutableStateOf("Week") }
    val periods = listOf("Day", "Week", "Month", "Year")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Monitoring", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // VIP Monitoring Banner
            item {
                VIPHealthMonitoringBanner()
            }

            // Period Filter
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(periods) { period ->
                        FilterChip(
                            onClick = { selectedPeriod = period },
                            label = { Text(period) },
                            selected = selectedPeriod == period
                        )
                    }
                }
            }

            // Vital Signs Cards
            item {
                Text(
                    "Vital Signs",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(mockVitalSigns) { vitalSign ->
                        VitalSignCard(vitalSign = vitalSign)
                    }
                }
            }

            // Health Metrics
            item {
                Text(
                    "Health Metrics",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                HealthMetricsChart()
            }

            // Recent Activities
            item {
                Text(
                    "Recent Activities",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(mockHealthActivities) { activity ->
                HealthActivityCard(activity = activity)
            }
        }
    }
}

@Composable
private fun VIPHealthMonitoringBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.MonitorHeart,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "VIP Health Monitoring",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Real-time • AI-powered • Personalized",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun VitalSignCard(vitalSign: VitalSign) {
    Card(
        modifier = Modifier.width(140.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                vitalSign.icon,
                contentDescription = null,
                tint = vitalSign.color,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                vitalSign.name,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                vitalSign.value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                vitalSign.unit,
                fontSize = 10.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                vitalSign.status,
                fontSize = 10.sp,
                color = when (vitalSign.status) {
                    "Normal" -> Color(0xFF4CAF50)
                    "High" -> Color(0xFFE53935)
                    "Low" -> Color(0xFFFF9800)
                    else -> Color.Gray
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HealthMetricsChart() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Weekly Health Trends",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Simple mock chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Health Trends Chart",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "Interactive chart would be here",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
private fun HealthActivityCard(activity: HealthActivity) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(activity.color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    activity.icon,
                    contentDescription = null,
                    tint = activity.color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    activity.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    activity.description,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    activity.time,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            if (activity.value != null) {
                Text(
                    activity.value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = activity.color
                )
            }
        }
    }
}
data class VitalSign(
    val name: String,
    val value: String,
    val unit: String,
    val status: String,
    val icon: ImageVector,
    val color: Color
)

data class HealthActivity(
    val title: String,
    val description: String,
    val time: String,
    val value: String? = null,
    val icon: ImageVector,
    val color: Color
)

val mockVitalSigns = listOf(
    VitalSign(
        name = "Heart Rate",
        value = "72",
        unit = "bpm",
        status = "Normal",
        icon = Icons.Default.Favorite,
        color = Color(0xFFE91E63)
    ),
    VitalSign(
        name = "Blood Pressure",
        value = "120/80",
        unit = "mmHg",
        status = "Normal",
        icon = Icons.Default.MonitorHeart,
        color = Color(0xFF3F51B5)
    ),
    VitalSign(
        name = "Oxygen Level",
        value = "97",
        unit = "%",
        status = "Normal",
        icon = Icons.Default.Air,
        color = Color(0xFF009688)
    ),
    VitalSign(
        name = "Temperature",
        value = "36.6",
        unit = "°C",
        status = "Normal",
        icon = Icons.Default.DeviceThermostat,
        color = Color(0xFFFF5722)
    )
)

val mockHealthActivities = listOf(
    HealthActivity(
        title = "Morning Walk",
        description = "Walked 2 km in 25 minutes",
        time = "Today • 7:00 AM",
        value = "2 km",
        icon = Icons.Default.DirectionsWalk,
        color = Color(0xFF4CAF50)
    ),
    HealthActivity(
        title = "Water Intake",
        description = "Drank 1.5 liters",
        time = "Today • 9:30 AM",
        value = "1.5L",
        icon = Icons.Default.Opacity,
        color = Color(0xFF03A9F4)
    ),
    HealthActivity(
        title = "Sleep",
        description = "Slept for 7 hours",
        time = "Yesterday • 11:00 PM",
        value = "7h",
        icon = Icons.Default.Bedtime,
        color = Color(0xFF9C27B0)
    ),
    HealthActivity(
        title = "Calories Burned",
        description = "Burned 350 kcal",
        time = "Yesterday • 6:00 PM",
        value = "350 kcal",
        icon = Icons.Default.LocalFireDepartment,
        color = Color(0xFFFF9800)
    )
)
package com.example.a2zcare.presentation.screens.vip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.navegation.bottomNavItems
import com.example.a2zcare.presentation.screens.home.BottomNavigationBar
import com.example.a2zcare.presentation.theme.backgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VIPScreen(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val selectedIndex = bottomNavItems.indexOfFirst { it.route == currentDestination?.route }
        .takeIf { it >= 0 } ?: 2


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("VIP Healthcare", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Notification.route) }) {
                        Badge(
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("3")
                        }
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                items = bottomNavItems,
                onItemSelected = { index ->
                    navController.navigate(bottomNavItems[index].route) {
                        popUpTo("vip") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(padding)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    // VIP Status Card
                    VIPStatusCard()

                    // Quick Actions
                    QuickActionsSection(navController)

                    // Health Summary
                    HealthSummarySection()

                    // Upcoming Appointments
                    UpcomingAppointmentsSection(navController)

                    // Health Tips
                    HealthTipsSection()
                }
            }
        }
    }
}

@Composable
private fun VIPStatusCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50)
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Stars,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "VIP Member",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Premium healthcare services at your fingertips",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
                Text(
                    "Member since: January 2024",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun QuickActionsSection(navController: NavController) {
    Text(
        "Quick Actions",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(quickActionItems) { item ->
            QuickActionCard(
                icon = item.icon,
                title = item.title,
                subtitle = item.subtitle,
                color = item.color,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Text(
                subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun HealthSummarySection() {
    Text(
        "Health Summary",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(healthSummaryItems) { item ->
            HealthSummaryCard(
                title = item.title,
                value = item.value,
                unit = item.unit,
                icon = item.icon,
                color = item.color
            )
        }
    }
}

@Composable
private fun HealthSummaryCard(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    value,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    unit,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                title,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
private fun UpcomingAppointmentsSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Upcoming Appointments",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = { navController.navigate("appointments") }) {
            Text("View All")
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { navController.navigate("chat_screen") }
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
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.VideoCall,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "Dr. Sarah Johnson",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    "Cardiologist",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Text(
                    "Tomorrow, 10:00 AM",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun HealthTipsSection() {
    Text(
        "Health Tips",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(healthTips) { tip ->
            HealthTipCard(tip)
        }
    }
}

@Composable
private fun HealthTipCard(tip: String) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(80.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                tip,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        }
    }
}

// Data for UI
data class QuickActionItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val color: Color,
    val route: String
)

val quickActionItems = listOf(
    QuickActionItem(
        Icons.Default.Medication,
        "Medicine",
        "Order now",
        Color(0xFF4CAF50),
        "vip_medicine_screen"
    ),
    QuickActionItem(
        Icons.Default.VideoCall,
        "Consultation",
        "Book doctor",
        Color(0xFF2196F3),
        "vip_doctor_consultations_screen"
    ),
    QuickActionItem(
        Icons.Default.Assignment,
        "Records",
        "View history",
        Color(0xFF9C27B0),
        "vip_health_records_screen"
    ),
    QuickActionItem(
        Icons.Default.MonitorHeart,
        "Health",
        "Track vitals",
        Color(0xFFFF9800),
        "consulting_chat_screen"
    )
)

data class HealthSummaryItem(
    val title: String,
    val value: String,
    val unit: String,
    val icon: ImageVector,
    val color: Color
)

val healthSummaryItems = listOf(
    HealthSummaryItem(
        "Blood Pressure",
        "120/80",
        "mmHg",
        Icons.Default.MonitorHeart,
        Color(0xFFFF5252)
    ),
    HealthSummaryItem(
        "Heart Rate",
        "72",
        "bpm",
        Icons.Default.Favorite,
        Color(0xFFE91E63)
    ),
    HealthSummaryItem("Weight", "70", "kg", Icons.Default.Scale, Color(0xFF9C27B0)),
    HealthSummaryItem(
        "Steps",
        "8,420",
        "today",
        Icons.Default.DirectionsWalk,
        Color(0xFF4CAF50)
    )
)

val healthTips = listOf(
    "💧 Drink at least 8 glasses of water daily for optimal hydration",
    "🚶‍♀️ Take a 10-minute walk after meals to improve digestion",
    "😴 Maintain a consistent sleep schedule for better mental health",
    "🥗 Include more vegetables and fruits in your daily diet",
    "🧘‍♀️ Practice 5 minutes of meditation daily to reduce stress"
)

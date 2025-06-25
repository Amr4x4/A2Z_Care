package com.example.a2zcare.presentation.screens.water_tracking


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.presentation.viewmodel.water_tracking_view_model.WaterTrackingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterTrackingScreen(
    navController: NavController,
    viewModel: WaterTrackingViewModel = hiltViewModel()
) {
    val dailyProgress by viewModel.dailyProgress.collectAsState()
    val intervals by viewModel.intervals.collectAsState()
    val glassCount by viewModel.glassCount.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF4FC3F7),
                        Color(0xFF29B6F6),
                        Color(0xFF03A9F4)
                    )
                )
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "💧 Water Tracker",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        // Progress Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Today's Progress",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1565C0)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Circular Progress
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(160.dp)
                ) {
                    CircularProgressIndicator(
                        progress = dailyProgress.goalPercentage / 100f,
                        modifier = Modifier.size(160.dp),
                        strokeWidth = 12.dp,
                        color = Color(0xFF4CAF50),
                        trackColor = Color(0xFFE0E0E0)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${dailyProgress.achievedAmount}ml",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1565C0)
                        )
                        Text(
                            text = "of ${dailyProgress.targetAmount}ml",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "${dailyProgress.goalPercentage.toInt()}%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "🥤 Glasses: $glassCount",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1565C0)
                )
            }
        }

        // Quick Add Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clickable { viewModel.incrementGlassCountNow() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "💧 Drink Water (250ml)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Glass Size Options
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Glass Sizes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1565C0),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    com.example.a2zcare.domain.entities.GlassSize.values().forEach { glassSize ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.addWaterIntake(glassSize) }
                                .padding(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        Color(0xFF2196F3).copy(alpha = 0.1f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (glassSize) {
                                        com.example.a2zcare.domain.entities.GlassSize.SMALL -> "🥃"
                                        com.example.a2zcare.domain.entities.GlassSize.MEDIUM -> "🥤"
                                        com.example.a2zcare.domain.entities.GlassSize.LARGE -> "🍺"
                                        com.example.a2zcare.domain.entities.GlassSize.BOTTLE -> "🍼"
                                    },
                                    fontSize = 20.sp
                                )
                            }
                            Text(
                                text = "${glassSize.ml}ml",
                                fontSize = 12.sp,
                                color = Color(0xFF1565C0),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Reminder Intervals
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "🔔 Reminder Intervals",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1565C0),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(intervals) { interval ->
                        IntervalChip(
                            interval = interval,
                            onClick = { viewModel.onIntervalClick(interval.minute) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Motivational Message
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
        ) {
            Text(
                text = when {
                    dailyProgress.goalPercentage >= 100f -> "🎉 Congratulations! You've reached your daily goal!"
                    dailyProgress.goalPercentage >= 75f -> "💪 Almost there! Keep going!"
                    dailyProgress.goalPercentage >= 50f -> "👍 Great progress! You're halfway there!"
                    dailyProgress.goalPercentage >= 25f -> "🌱 Good start! Keep hydrating!"
                    else -> "🚀 Start your hydration journey today!"
                },
                fontSize = 14.sp,
                color = Color(0xFF1565C0),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun IntervalChip(
    interval: com.example.a2zcare.domain.entities.Interval,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (interval.selected) {
                Color(0xFF4CAF50)
            } else {
                Color(0xFFE3F2FD)
            }
        )
    ) {
        Text(
            text = interval.displayName,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (interval.selected) Color.White else Color(0xFF1565C0)
        )
    }
}

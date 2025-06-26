package com.example.a2zcare.presentation.screens.water_tracking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a2zcare.R
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.domain.entities.GlassSize
import com.example.a2zcare.presentation.common_ui.MiniTopBar
import com.example.a2zcare.presentation.theme.*
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
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val errorState by viewModel.errorState.collectAsState()

    var selectedGlassSize by remember { mutableStateOf(GlassSize.MEDIUM) }

    Scaffold(
        topBar = {
            MiniTopBar(
                title = "💧 Water Tracker",
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(innerPadding)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Error message if any
                    errorState?.let { error ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = errorBackground)
                        ) {
                            Text(
                                text = "⚠️ $error",
                                color = errorRed,
                                modifier = Modifier.padding(16.dp),
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Refresh indicator
                    if (isRefreshing) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = lightBlueBackground)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = primaryBlue
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Refreshing data...",
                                    color = primaryBlue,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // Progress Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = fieldCardColor,
                            contentColor = fieldCardColor,
                            disabledContainerColor = fieldCardColor,
                            disabledContentColor = fieldCardColor,
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Today's Progress",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryBlue
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(160.dp)
                            ) {
                                CircularProgressIndicator(
                                    progress = dailyProgress.goalPercentage / 100f,
                                    modifier = Modifier.size(160.dp),
                                    strokeWidth = 12.dp,
                                    color = successGreen,
                                    trackColor = progressTrack
                                )

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "${dailyProgress.achievedAmount}ml",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryBlue
                                    )
                                    Text(
                                        text = "of ${dailyProgress.targetAmount}ml",
                                        fontSize = 14.sp,
                                        color = secondaryText
                                    )
                                    Text(
                                        text = "${dailyProgress.goalPercentage.toInt()}%",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = successGreen
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "🥤 Glasses: $glassCount",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = primaryBlue
                            )
                        }
                    }

                    // Glass Size Selection Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = fieldCardColor)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Select Glass Size",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryBlue,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                GlassSize.values().forEach { glassSize ->
                                    val isSelected = selectedGlassSize == glassSize

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(
                                                if (isSelected) selectedItemBackground else Color.Transparent
                                            )
                                            .clickable {
                                                selectedGlassSize = glassSize
                                            }
                                            .padding(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .background(
                                                    if (isSelected)
                                                        whiteText.copy(alpha = 0.2f)
                                                    else
                                                        lightBlue.copy(alpha = 0.1f),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Image(
                                                painter = when (glassSize) {
                                                    GlassSize.SMALL -> painterResource(R.drawable.water_150ml)
                                                    GlassSize.MEDIUM -> painterResource(R.drawable.water_250ml)
                                                    GlassSize.LARGE -> painterResource(R.drawable.water_350ml)
                                                    GlassSize.BOTTLE -> painterResource(R.drawable.water_bottle)
                                                },
                                                contentDescription = "Glass With Different sizes"
                                            )
                                        }
                                        Text(
                                            text = "${glassSize.ml}ml",
                                            fontSize = 12.sp,
                                            color = if (isSelected) whiteText else primaryBlue,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Drink Water Button (Green Card)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                if (!isLoading) {
                                    viewModel.addWaterIntake(selectedGlassSize)
                                }
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isLoading) lightGreen else successGreen
                        )
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
                                    color = whiteText,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Adding...",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = whiteText
                                )
                            } else {
                                Text(
                                    text = "💧 Drink Water (${selectedGlassSize.ml}ml)",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = whiteText
                                )
                            }
                        }
                    }

                    // Reminder Intervals
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = fieldCardColor)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "🔔 Reminder Intervals",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryBlue,
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
                        colors = CardDefaults.cardColors(containerColor = fieldCardColor)
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
                            color = primaryBlue,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
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
                selectedItemBackground
            } else {
                unselectedItemBackground
            }
        )
    ) {
        Text(
            text = interval.displayName,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (interval.selected) whiteText else primaryBlue
        )
    }
}
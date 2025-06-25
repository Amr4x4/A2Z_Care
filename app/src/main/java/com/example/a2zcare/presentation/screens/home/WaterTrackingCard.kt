package com.example.a2zcare.presentation.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.R
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.darkBlue
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.theme.lightBlue
import com.example.a2zcare.presentation.theme.lightGreen
import com.example.a2zcare.presentation.viewmodel.water_tracking_view_model.WaterTrackingViewModel

@Composable
fun WaterTrackingCard(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: WaterTrackingViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry) {
        if (navBackStackEntry?.destination?.route == "home") {
            viewModel.refreshData()
        }
    }

    val dailyProgress by viewModel.dailyProgress.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val progress = (dailyProgress.goalPercentage / 100f).coerceIn(0f, 1f)

    Card(
        modifier = modifier
            .height(160.dp)
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = fieldCardColor,
            contentColor = fieldCardColor,
            disabledContainerColor = fieldCardColor,
            disabledContentColor = fieldCardColor,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 12.dp)
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Water Intake",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 18.sp, lineHeight = 20.sp),
                )

                Text(
                    text = "${dailyProgress.achievedAmount}ml / ${dailyProgress.targetAmount}ml",
                    color = darkBlue,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp)
                )

                Text(
                    text = "${dailyProgress.goalPercentage.toInt()}% Complete",
                    color = lightGreen,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    ) {
                        val barHeight = 12.dp.toPx()
                        val cornerRadius = 6.dp.toPx()

                        // Background track
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.3f),
                            topLeft = Offset(0f, (size.height - barHeight) / 2),
                            size = Size(size.width, barHeight),
                            cornerRadius = CornerRadius(cornerRadius)
                        )

                        // Progress fill
                        val progressWidth = size.width * progress
                        if (progressWidth > 0) {
                            drawRoundRect(
                                color = lightBlue,
                                topLeft = Offset(0f, (size.height - barHeight) / 2),
                                size = Size(progressWidth, barHeight),
                                cornerRadius = CornerRadius(cornerRadius)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.water_drop),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(40.dp)
                )

                IconButton(
                    onClick = {
                        navController.navigate(Screen.WaterTracker.route)
                    },
                    modifier = Modifier.size(37.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowCircleRight,
                        contentDescription = "Go to Water Tracking",
                        tint = Color.LightGray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewWaterTrackingCard() {
    WaterTrackingCard(navController = rememberNavController())
}
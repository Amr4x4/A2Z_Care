package com.example.a2zcare.presentation.screens.home.onboarding_heart_home_card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.decode.GifDecoder
import com.example.a2zcare.presentation.model.LiveStatusManager
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.theme.successGreen
import com.example.a2zcare.presentation.viewmodel.HealthDataViewModel

@Composable
fun HeartDiseasePredictionCard(
    viewModel: HealthDataViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context).components { add(GifDecoder.Factory()) }.build()
    }

    var isHeartDiseaseInformationDialogOpen by rememberSaveable { mutableStateOf(false) }

    val isOnline by LiveStatusManager.isOnline.collectAsState()
    val lastSeen by LiveStatusManager.lastSeen.collectAsState()
    val heartDiseasePrediction by viewModel.heartDiseasePrediction.collectAsState()
    val shouldShowWarning by viewModel.shouldShowWarningNotification.collectAsState()
    val warningMessage by viewModel.warningMessage.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Warning Dialog
    if (shouldShowWarning) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissWarningNotification() },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        tint = Color(0xFFFF6B6B)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Heart Disease Warning",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Text(
                    warningMessage,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.dismissWarningNotification() },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B)
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Calculate red dot visibility
    val countPerMinute = viewModel.uiState.value.heartRateData?.heartrate ?: 0
    val showRedDot = when (heartDiseasePrediction) {
        "Supraventricular premature" -> countPerMinute > 6
        "Premature ventricular contraction" -> countPerMinute > 5
        "Fusion of ventricular and normal" -> countPerMinute > 1
        "Unclassifiable" -> countPerMinute > 1
        else -> false
    }

    // Show dialog with advice based on prediction
    if (isHeartDiseaseInformationDialogOpen) {
        HeartDiseaseAdviceDialog(
            isOpen = true,
            prediction = heartDiseasePrediction,
            countPerMinute = countPerMinute,
            onDismissRequest = { isHeartDiseaseInformationDialogOpen = false },
            onConfirmButtonClick = { isHeartDiseaseInformationDialogOpen = false }
        )
    }

    // Track prediction counts in the last minute
    val predictionCounts = remember { mutableStateOf(mutableMapOf(
        "Normal" to 0,
        "Supraventricular premature" to 0,
        "Premature ventricular contraction" to 0,
        "Fusion of ventricular and normal" to 0,
        "Unclassifiable" to 0
    )) }
    val predictionTimestamps = remember { mutableStateListOf<Pair<Long, String>>() }

    // Update counts every time prediction changes
    LaunchedEffect(heartDiseasePrediction) {
        if (heartDiseasePrediction.isNotBlank()) {
            val currentTime = System.currentTimeMillis()
            predictionTimestamps.add(currentTime to heartDiseasePrediction)
            val oneMinuteAgo = currentTime - 60_000
            predictionTimestamps.removeAll { it.first < oneMinuteAgo }
            val counts = mutableMapOf(
                "Normal" to 0,
                "Supraventricular premature" to 0,
                "Premature ventricular contraction" to 0,
                "Fusion of ventricular and normal" to 0,
                "Unclassifiable" to 0
            )
            predictionTimestamps.forEach { (_, pred) ->
                if (counts.containsKey(pred)) counts[pred] = counts[pred]!! + 1
            }
            predictionCounts.value = counts
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(380.dp)
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = fieldCardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(fieldCardColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = if (isOnline) Color.Green else Color.Red,
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = if (isOnline) "Online" else "Offline",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray
                            )
                            if (!isOnline) {
                                Text(
                                    text = "Last ${LiveStatusManager.formatTimestamp(lastSeen)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }

                    // Title with Icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Heart Disease",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                    }
                    IconButton(
                        onClick = { isHeartDiseaseInformationDialogOpen = true },
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = Color(0xFFFFC107).copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                    ) {
                        Box {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info",
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(20.dp)
                            )
                            if (showRedDot) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            color = Color(0xFFFF6B6B),
                                            shape = CircleShape
                                        )
                                        .align(Alignment.TopEnd)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Monitoring Heart Activity...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF6C757D)
                            )
                        }
                    }
                } else {
                    // Status Display
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isHeartDiseaseNormal(heartDiseasePrediction))
                                Color(0xFF4CAF50).copy(alpha = 0.1f)
                            else
                                Color(0xFFFF6B6B).copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Current Status",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF6C757D),
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = heartDiseasePrediction,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = if (isHeartDiseaseNormal(heartDiseasePrediction))
                                        Color(0xFF4CAF50)
                                    else
                                        Color(0xFFFF6B6B),
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Predictions Table
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Last Minute Analysis",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF212529)
                            )
                            Icon(
                                imageVector = Icons.Default.MonitorHeart,
                                contentDescription = "Monitor",
                                tint = Color(0xFF6C757D),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Headers
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf("Normal", "Supraventricular", "Premature", "Fusion", "Unclassifiable").forEach { label ->
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF6C757D),
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Values
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val counts = listOf(
                                predictionCounts.value["Normal"] ?: 0,
                                predictionCounts.value["Supraventricular premature"] ?: 0,
                                predictionCounts.value["Premature ventricular contraction"] ?: 0,
                                predictionCounts.value["Fusion of ventricular and normal"] ?: 0,
                                predictionCounts.value["Unclassifiable"] ?: 0
                            )

                            counts.forEachIndexed { index, count ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            color = if (index == 0) Color(0xFF4CAF50).copy(alpha = 0.1f)
                                            else Color(0xFFFF6B6B).copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = count.toString(),
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = if (index == 0) Color(0xFF4CAF50) else Color(0xFFFF6B6B),
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                if (index < counts.size - 1) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                            }
                        }
                    }
                }

                // Error Message
                uiState.errorMessage?.let { errorMessage ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFF6B6B).copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = Color(0xFFFF6B6B),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFFF6B6B)
                            )
                        }
                    }
                }
            }
        }
    }
}
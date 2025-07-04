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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.a2zcare.presentation.model.LiveStatusManager
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.viewmodel.HealthDataViewModel
import kotlinx.coroutines.delay

@Composable
fun HeartDiseasePredictionCard(
    viewModel: HealthDataViewModel,
    modifier: Modifier = Modifier,
    trackingOff: Boolean = false
) {
    var isHeartDiseaseInformationDialogOpen by rememberSaveable { mutableStateOf(false) }
    var showWarningDialog by rememberSaveable { mutableStateOf(false) }
    var warningMessage by remember { mutableStateOf("") }

    val isOnline by LiveStatusManager.isOnline.collectAsState()
    val lastSeen by LiveStatusManager.lastSeen.collectAsState()
    val heartDiseasePrediction by viewModel.heartDiseasePrediction.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    // Track prediction counts in the last minute
    val predictionTimestamps = remember { mutableStateListOf<Pair<Long, String>>() }

    // Derived state for current counts
    val predictionCounts by remember {
        derivedStateOf {
            val currentTime = System.currentTimeMillis()
            val oneMinuteAgo = currentTime - 60_000

            // Filter predictions from the last minute
            val recentPredictions = predictionTimestamps.filter { it.first > oneMinuteAgo }

            // Count each prediction type
            mutableMapOf(
                "Normal" to recentPredictions.count { it.second == "Normal" },
                "Supraventricular premature" to recentPredictions.count { it.second == "Supraventricular premature" },
                "Premature ventricular contraction" to recentPredictions.count { it.second == "Premature ventricular contraction" },
                "Fusion of ventricular and normal" to recentPredictions.count { it.second == "Fusion of ventricular and normal" },
                "Unclassifiable" to recentPredictions.count { it.second == "Unclassifiable" }
            )
        }
    }

    // Check for warnings based on prediction counts
    val shouldShowWarning by remember {
        derivedStateOf {
            val counts = predictionCounts
            counts["Supraventricular premature"]!! > 6 ||
                    counts["Premature ventricular contraction"]!! > 5 ||
                    counts["Fusion of ventricular and normal"]!! > 1 ||
                    counts["Unclassifiable"]!! > 1
        }
    }

    // Update warning message based on current counts
    LaunchedEffect(shouldShowWarning, predictionCounts) {
        if (shouldShowWarning) {
            val counts = predictionCounts
            val messages = mutableListOf<String>()

            if (counts["Supraventricular premature"]!! > 6) {
                messages.add("⚠️ Supraventricular Premature: ${counts["Supraventricular premature"]} detected per minute (>6)")
            }
            if (counts["Premature ventricular contraction"]!! > 5) {
                messages.add("⚠️ Premature Ventricular Contraction: ${counts["Premature ventricular contraction"]} detected per minute (>5)")
            }
            if (counts["Fusion of ventricular and normal"]!! > 1) {
                messages.add("⚠️ Fusion of Ventricular and Normal: ${counts["Fusion of ventricular and normal"]} detected per minute (>1)")
            }
            if (counts["Unclassifiable"]!! > 1) {
                messages.add("⚠️ Unclassifiable: ${counts["Unclassifiable"]} detected per minute (>1)")
            }

            warningMessage = messages.joinToString("\n\n") + "\n\n" + getAdviceForCondition(
                heartDiseasePrediction
            )
            showWarningDialog = true
        }
    }

    // Update prediction counts every time prediction changes
    LaunchedEffect(heartDiseasePrediction) {
        if (heartDiseasePrediction.isNotBlank() && heartDiseasePrediction != "Loading...") {
            val currentTime = System.currentTimeMillis()
            predictionTimestamps.add(currentTime to heartDiseasePrediction)

            // Clean up old timestamps (older than 1 minute)
            val oneMinuteAgo = currentTime - 60_000
            predictionTimestamps.removeAll { it.first < oneMinuteAgo }
        }
    }

    // Periodic cleanup of old timestamps
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000) // Clean up every 5 seconds
            val currentTime = System.currentTimeMillis()
            val oneMinuteAgo = currentTime - 60_000
            predictionTimestamps.removeAll { it.first < oneMinuteAgo }
        }
    }

    // Warning Dialog
    if (showWarningDialog) {
        AlertDialog(
            onDismissRequest = { showWarningDialog = false },
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
                    onClick = { showWarningDialog = false },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF6B6B)
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }

    // Calculate red dot visibility based on current prediction counts
    val showRedDot = shouldShowWarning

    // Show dialog with advice based on prediction
    if (isHeartDiseaseInformationDialogOpen) {
        HeartDiseaseAdviceDialog(
            isOpen = true,
            prediction = heartDiseasePrediction,
            predictionCounts = predictionCounts,
            onDismissRequest = { isHeartDiseaseInformationDialogOpen = false },
            onConfirmButtonClick = { isHeartDiseaseInformationDialogOpen = false }
        )
    }

    if (trackingOff) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(380.dp)
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = fieldCardColor)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Tracking is OFF",
                    color = Color.Gray,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        return
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
                            listOf(
                                "Normal",
                                "Supraventricular",
                                "Premature",
                                "Fusion",
                                "Unclassifiable"
                            ).forEach { label ->
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
                                predictionCounts["Normal"] ?: 0,
                                predictionCounts["Supraventricular premature"] ?: 0,
                                predictionCounts["Premature ventricular contraction"] ?: 0,
                                predictionCounts["Fusion of ventricular and normal"] ?: 0,
                                predictionCounts["Unclassifiable"] ?: 0
                            )

                            counts.forEachIndexed { index, count ->
                                val isWarning = when (index) {
                                    1 -> count > 6 // Supraventricular premature
                                    2 -> count > 5 // Premature ventricular contraction
                                    3 -> count > 1 // Fusion of ventricular and normal
                                    4 -> count > 1 // Unclassifiable
                                    else -> false
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            color = if (index == 0) Color(0xFF4CAF50).copy(alpha = 0.1f)
                                            else if (isWarning) Color(0xFFFF6B6B).copy(alpha = 0.2f)
                                            else Color(0xFFFF6B6B).copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = count.toString(),
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = if (index == 0) Color(0xFF4CAF50)
                                        else if (isWarning) Color(0xFFFF6B6B)
                                        else Color(0xFFFF6B6B).copy(alpha = 0.7f),
                                        fontWeight = if (isWarning) FontWeight.ExtraBold else FontWeight.Bold
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

@Composable
fun HeartDiseaseAdviceDialog(
    isOpen: Boolean,
    prediction: String,
    predictionCounts: Map<String, Int>,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Color(0xFF2196F3)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Heart Disease Information",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column {
                    Text(
                        "Current Status: $prediction",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isHeartDiseaseNormal(prediction)) Color(0xFF4CAF50) else Color(
                            0xFFFF6B6B
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        "Last Minute Summary:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    predictionCounts.forEach { (type, count) ->
                        if (count > 0) {
                            Text(
                                "• $type: $count",
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (type == "Normal") Color(0xFF4CAF50) else Color(
                                    0xFFFF6B6B
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        getAdviceForCondition(prediction),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = onConfirmButtonClick,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }
}

private fun getAdviceForCondition(prediction: String): String {
    return when (prediction) {
        "Supraventricular premature" -> """
            🔹 Advice for Supraventricular Premature Beats:
            • Take deep, slow breaths
            • Avoid caffeine and alcohol
            • Reduce stress through relaxation techniques
            • Consult your doctor if symptoms persist
            • Monitor frequency - more than 6 per minute requires medical attention
        """.trimIndent()

        "Premature ventricular contraction" -> """
            🔹 Advice for Premature Ventricular Contractions:
            • Rest and avoid strenuous activity
            • Limit caffeine and stimulants
            • Practice stress management
            • Stay hydrated
            • Seek medical attention if more than 5 per minute
        """.trimIndent()

        "Fusion of ventricular and normal" -> """
            🔹 Advice for Fusion Beats:
            • Monitor closely as this indicates irregular heart rhythm
            • Avoid excessive physical activity
            • Contact your healthcare provider
            • More than 1 per minute requires immediate medical evaluation
        """.trimIndent()

        "Unclassifiable" -> """
            🔹 Advice for Unclassifiable Beats:
            • These irregular patterns need medical evaluation
            • Avoid strenuous activities
            • Monitor symptoms closely
            • More than 1 per minute requires immediate medical attention
        """.trimIndent()

        "Normal" -> """
            ✅ Your heart rhythm appears normal.
            • Continue regular physical activity
            • Maintain a healthy lifestyle
            • Keep monitoring as part of routine health care
        """.trimIndent()

        else -> """
            🔹 General Heart Health Advice:
            • Monitor your heart rate regularly
            • Maintain a healthy diet
            • Exercise regularly but within your limits
            • Consult healthcare providers for any concerns
        """.trimIndent()
    }
}
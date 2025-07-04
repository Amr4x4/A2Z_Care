package com.example.a2zcare.presentation.screens.home.onboarding_heart_home_card

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.a2zcare.R
import com.example.a2zcare.presentation.model.LiveStatusManager
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.viewmodel.HealthDataViewModel
import java.util.Locale

@Composable
fun HeartRateCard(
    viewModel: HealthDataViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context).components { add(GifDecoder.Factory()) }.build()
    }
    var isHeartRateInformationDialogOpen by rememberSaveable { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val isOnline by LiveStatusManager.isOnline.collectAsState()
    val lastSeen by LiveStatusManager.lastSeen.collectAsState()

    // Get heart rate data from ViewModel
    val heartRateData = uiState.heartRateData
    val beats = heartRateData?.heartrate ?: 0
    val status = heartRateData?.category ?: "Unknown"

    // Show dialog with advice based on heart rate status
    if (isHeartRateInformationDialogOpen) {
        HeartRateAdviceDialog(
            isOpen = true,
            status = status,
            onDismissRequest = { isHeartRateInformationDialogOpen = false },
            onConfirmButtonClick = { isHeartRateInformationDialogOpen = false }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(12.dp),
        colors = CardDefaults.cardColors(containerColor = fieldCardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    if (isOnline) Color.Green else Color.Red,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isOnline) "Online" else "Offline",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                    Text(
                        text = if (isOnline) "" else "Last ${
                            LiveStatusManager.formatTimestamp(lastSeen)
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }

                Spacer(modifier = Modifier.width(1.dp))
                Text(
                    text = "Heart Rate",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.width(20.dp))

                IconButton(onClick = { isHeartRateInformationDialogOpen = true }) {
                    Box {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Heart Rate info",
                            tint = Color.Yellow
                        )
                        if (status.lowercase(Locale.ROOT) != "normal") {
                            Canvas(
                                modifier = Modifier
                                    .size(6.dp)
                                    .align(Alignment.TopEnd)
                            ) {
                                drawCircle(
                                    color = Color.Red,
                                    radius = size.minDimension / 2,
                                    center = Offset(x = size.width, y = 0f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Show loading indicator if data is being fetched
            if (uiState.isLoading && heartRateData == null) {
                Box(
                    modifier = Modifier
                        .size(250.dp, 160.dp)
                        .background(fieldCardColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(context)
                            .data(
                                if (status.lowercase(Locale.ROOT) == "normal")
                                    R.drawable.heart_beat_normal
                                else R.drawable.heart_beat_not_normal
                            )
                            .build(),
                        imageLoader = imageLoader
                    ),
                    contentDescription = "Heart Rate GIF",
                    modifier = Modifier
                        .size(250.dp, 160.dp)
                        .background(fieldCardColor),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Heart Beats: $beats b/m",
                color = if (status.lowercase(Locale.ROOT) == "normal") Color.Green else Color.Red
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Status: $status",
                color = if (status.lowercase(Locale.ROOT) == "normal") Color.Green else Color.Red,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp)
            )
        }
    }
}


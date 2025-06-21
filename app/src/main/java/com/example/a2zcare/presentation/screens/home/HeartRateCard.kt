package com.example.a2zcare.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.a2zcare.R
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.theme.lightRed
import java.util.Locale

@Composable
fun HeartRateCard(
    status: String = "Not Normal",
    numSPB: Int = 122,
    numDBP: Int = 80
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()
    }
    var isHeartRateInformationDialogOpen by rememberSaveable { mutableStateOf(false) }
    HeartRateInformationDialog(
        isOpen = isHeartRateInformationDialogOpen,
        title = "Heart rate Status",
        onDismissRequest = { isHeartRateInformationDialogOpen = false },
        onConfirmButtonClick = {
            isHeartRateInformationDialogOpen = false
        }
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = fieldCardColor,
            contentColor = fieldCardColor,
            disabledContainerColor = fieldCardColor,
            disabledContentColor = fieldCardColor,
        )
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
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = "Heart Rate",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.DarkGray
                )
                IconButton(
                    onClick = {
                        isHeartRateInformationDialogOpen = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Heart Rate information",
                        tint = Color.Yellow
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(
                            if (status.toLowerCase(Locale.ROOT) == "normal")
                                R.drawable.heart_rate_good
                            else
                            R.drawable.heart_rate_not_good
                        )
                        .build(),
                    imageLoader = imageLoader
                ),
                contentDescription = "Heart Rate GIF",
                modifier = Modifier
                    .size(250.dp, 160.dp)
                    .background(fieldCardColor),
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SBP: ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
                Text(
                    text = "$numSPB/120",
                    style = MaterialTheme.typography.bodySmall,
                    color = lightRed
                )
                Spacer(modifier = Modifier.width(50.dp))
                Text(
                    text = "DBP: ",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
                Text(
                    text = "$numDBP/80",
                    style = MaterialTheme.typography.bodySmall,
                    color = lightRed
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Status: $status",
                color = if (status.toLowerCase(Locale.ROOT) == "normal") Color.Green else Color.Red,
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 16.sp, lineHeight = 30.sp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHeartRateCard() {
    HeartRateCard("Normal")
}
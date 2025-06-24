package com.example.a2zcare.presentation.screens.tracker

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a2zcare.presentation.viewmodel.water_tracking.WaterAlarmViewModel
import com.example.a2zcare.presentation.viewmodel.water_tracking.WaterAlarmViewModelFactory
import com.example.a2zcare.data.local.entity.Interval

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterTrackingCard() {
    val context = LocalContext.current
    val viewModel: WaterAlarmViewModel = viewModel(
        factory = WaterAlarmViewModelFactory(context.applicationContext as Application)
    )
    val intervals by viewModel.intervals.observeAsState(emptyList())
    val glassCount by viewModel.glassCount.observeAsState(0)

    WaterTrackingContent(
        intervals = intervals,
        glassCount = glassCount,
        onIncrement = viewModel::incrementGlassCountNow,
        onIntervalClick = viewModel::onIntervalClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WaterTrackingContent(
    intervals: List<Interval>,
    glassCount: Int,
    onIncrement: () -> Unit,
    onIntervalClick: (Int) -> Unit,
) {
    Scaffold(topBar = { TopAppBar(title = { Text("Hydralarm") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Water Glasses Drank", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$glassCount", style = MaterialTheme.typography.displayLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onIncrement) {
                Text("I drank a glass")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Remind me every:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                intervals.forEach { interval ->
                    AssistChip(
                        onClick = { onIntervalClick(interval.minute) },
                        label = { Text("${interval.minute} min") },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (interval.selected)
                                MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWaterTrackingContent() {
    val mockIntervals = listOf(
        Interval(15, selected = false),
        Interval(30, selected = true),
        Interval(60, selected = false)
    )

    MaterialTheme {
        WaterTrackingContent(
            intervals = mockIntervals,
            glassCount = 3,
            onIncrement = {},
            onIntervalClick = {}
        )
    }
}

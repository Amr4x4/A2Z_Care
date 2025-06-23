package com.example.a2zcare.presentation.screens.tracker

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a2zcare.data.local.entity.Interval
import com.example.a2zcare.presentation.viewmodel.water_tracking.WaterAlarmViewModel
import com.example.a2zcare.presentation.viewmodel.water_tracking.WaterAlarmViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterTrackingCard(
    viewModel: WaterAlarmViewModel = viewModel(
        factory = WaterAlarmViewModelFactory(LocalContext.current.applicationContext as Application)
    )
) {
    val intervals by viewModel.intervals.observeAsState(emptyList())
    val glassCount by viewModel.glassCount.observeAsState(0)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hydralarm") }
            )
        }
    ) { padding ->
        MainBody(
            intervals,
            glassCount,
            onIntervalClick = viewModel::onIntervalClick,
            onIncrementClick = viewModel::incrementGlassCountNow,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun MainBody(
    intervals: List<Interval>,
    glassCount: Int,
    onIntervalClick: (Int) -> Unit,
    onIncrementClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Water Glasses Drank", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "$glassCount", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onIncrementClick) {
            Text("I drank a glass")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Remind me every:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        IntervalOptions(intervals, onIntervalClick)
    }
}

@Composable
fun IntervalOptions(intervals: List<Interval>, onIntervalClick: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        intervals.forEach { interval ->
            AssistChip(
                onClick = { onIntervalClick(interval.minute) },
                label = { Text("${interval.minute} min") },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (interval.selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WaterTrackingCard()
}

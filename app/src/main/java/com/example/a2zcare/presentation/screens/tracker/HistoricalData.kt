package com.example.a2zcare.presentation.screens.tracker

import co.yml.charts.common.model.Point
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.fieldColor
import com.example.a2zcare.presentation.viewmodel.HistoricalDataViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricalDataScreen(
    navController: NavController,
    viewModel: HistoricalDataViewModel = hiltViewModel()
) {
    val selectedCard by viewModel.selectedCard
    val isLoading by viewModel.isLoading
    val downloadStatus by viewModel.downloadStatus

    LaunchedEffect(downloadStatus) {
        if (downloadStatus != null) {
            // Optional: trigger Snackbar or Toast
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        TopAppBar(
            title = {
                Text(
                    "Historical Data",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = { viewModel.downloadPDF() }) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = "Download PDF",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = fieldColor,
                scrolledContainerColor = fieldColor
            )
        )

        downloadStatus?.let {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (it.contains("Successfully")) Color.Green.copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f)
                )
            ) {
                Text(it, modifier = Modifier.padding(16.dp), color = if (it.contains("Successfully")) Color.Green else Color.Red)
            }
        }

        if (selectedCard == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Select a metric to view historical data",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                HealthMetricCard("Blood Pressure", "View blood pressure trends", Icons.Default.Favorite, Color(0xFFE57373)) {
                    viewModel.selectCard("blood_pressure")
                }
                HealthMetricCard("Heart Disease", "Monitor predictions", Icons.Default.MonitorHeart, Color(0xFF81C784)) {
                    viewModel.selectCard("heart_disease")
                }
                HealthMetricCard("Heart Rate", "Track heart rate", Icons.Default.Favorite, Color(0xFF64B5F6)) {
                    viewModel.selectCard("heart_rate")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                TextButton(onClick = { viewModel.clearSelection() }, modifier = Modifier.align(Alignment.Start)) {
                    Text("\u2190 Back to Overview")
                }
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    when (selectedCard) {
                        "blood_pressure" -> BloodPressureChart(viewModel)
                        "heart_disease" -> HeartDiseaseChart(viewModel)
                        "heart_rate" -> HeartRateChart(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun HealthMetricCard(title: String, description: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(12.dp)).background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(description, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(16.dp).background(color, RoundedCornerShape(8.dp)))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 14.sp)
    }
}

@Composable
fun DataItem(timestamp: String, values: String, category: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Text(
                SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(parseTimestamp(timestamp)),
                fontSize = 12.sp, color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(values, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(category, fontSize = 14.sp, color = Color.Blue)
        }
    }
}

private fun parseTimestamp(timestamp: String): Date {
    val formats = listOf("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd")
    formats.forEach {
        try {
            return SimpleDateFormat(it, Locale.getDefault()).parse(timestamp) ?: Date()
        } catch (_: Exception) {}
    }
    return timestamp.toLongOrNull()?.let { Date(it) } ?: Date()
}

fun getDiseaseColor(disease: String): Color {
    return when (disease.lowercase()) {
        "normal" -> Color(0xFF4CAF50)
        "supraventricular premature" -> Color(0xFFFFC107)
        "premature ventricular contraction" -> Color(0xFFFF9800)
        "fusion of ventricular and normal" -> Color(0xFFFF5722)
        "unclassifiable" -> Color(0xFFF44336)
        else -> Color.Gray
    }
}
@Composable
fun BloodPressureChart(viewModel: HistoricalDataViewModel) {
    val bloodPressureData by viewModel.bloodPressureData

    if (bloodPressureData.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No blood pressure data available")
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Blood Pressure History",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Create chart data
        val sbpPoints = bloodPressureData.mapIndexed { index, data ->
            Point(index.toFloat(), data.sbp.toFloat())
        }


        val dbpPoints = bloodPressureData.mapIndexed { index, data ->
            Point(index.toFloat(), data.dbp.toFloat())
        }


        val xAxisData = AxisData.Builder()
            .axisStepSize(100.dp)
            .backgroundColor(Color.White)
            .steps(bloodPressureData.size - 1)
            .labelData { i ->
                if (i < bloodPressureData.size) {
                    SimpleDateFormat("MM/dd", Locale.getDefault())
                        .format(parseTimestamp(bloodPressureData[i].timestamp))
                } else ""
            }
            .labelAndAxisLinePadding(15.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(5)
            .backgroundColor(Color.White)
            .labelAndAxisLinePadding(20.dp)
            .labelData { i ->
                val yMin = 60
                val yMax = 180
                val step = (yMax - yMin) / 5
                (yMin + (i * step)).toString()
            }
            .build()

        val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = sbpPoints,
                        LineStyle(color = Color.Red, lineType = LineType.Straight()),
                        IntersectionPoint(color = Color.Red),
                        SelectionHighlightPoint(color = Color.Red),
                        ShadowUnderLine(alpha = 0.3f, brush = null),
                        SelectionHighlightPopUp()
                    ),
                    Line(
                        dataPoints = dbpPoints,
                        LineStyle(color = Color.Blue, lineType = LineType.Straight()),
                        IntersectionPoint(color = Color.Blue),
                        SelectionHighlightPoint(color = Color.Blue),
                        ShadowUnderLine(alpha = 0.3f, brush = null),
                        SelectionHighlightPopUp()
                    )
                )
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(),
            backgroundColor = Color.White
        )

        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = lineChartData
        )

        // Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LegendItem(color = Color.Red, text = "Systolic")
            LegendItem(color = Color.Blue, text = "Diastolic")
        }

        // Data list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(bloodPressureData.size) { index ->
                val data = bloodPressureData[index]
                DataItem(
                    timestamp = data.timestamp,
                    values = "SBP: ${data.sbp.toInt()} / DBP: ${data.dbp.toInt()}",
                    category = data.category
                )
            }
        }
    }
}

@Composable
fun HeartDiseaseChart(viewModel: HistoricalDataViewModel) {
    val heartDiseaseData by viewModel.heartDiseaseData

    if (heartDiseaseData.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No heart disease data available")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Heart Disease Predictions",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Disease distribution
        val diseaseGroups = heartDiseaseData.groupBy { it.diseases }

        diseaseGroups.forEach { (disease, records) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = getDiseaseColor(disease).copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = disease,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${records.size} occurrences",
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recent records
        Text(
            text = "Recent Records",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        heartDiseaseData.takeLast(10).forEach { data ->
            DataItem(
                timestamp = data.recordedAt,
                values = data.diseases,
                category = data.diseases
            )
        }
    }
}

@Composable
fun HeartRateChart(viewModel: HistoricalDataViewModel) {
    val heartRateData by viewModel.heartRateData

    if (heartRateData.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No heart rate data available")
        }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Heart Rate History",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Create chart data
        val heartRatePoints = heartRateData.mapIndexed { index, data ->
            Point(index.toFloat(), data.heartRate.toFloat())
        }


        val xAxisData = AxisData.Builder()
            .axisStepSize(100.dp)
            .backgroundColor(Color.White)
            .steps(heartRateData.size - 1)
            .labelData { i ->
                if (i < heartRateData.size) {
                    SimpleDateFormat("MM/dd", Locale.getDefault())
                        .format(parseTimestamp(heartRateData[i].recordedAt))
                } else ""
            }
            .labelAndAxisLinePadding(15.dp)
            .build()

        val yAxisData = AxisData.Builder()
            .steps(5)
            .backgroundColor(Color.White)
            .labelAndAxisLinePadding(20.dp)
            .labelData { i ->
                val yMin = 40
                val yMax = 120
                val step = (yMax - yMin) / 5
                (yMin + (i * step)).toString()
            }
            .build()

        val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = heartRatePoints,
                        LineStyle(color = Color(0xFF2196F3), lineType = LineType.Straight()),
                        IntersectionPoint(color = Color(0xFF2196F3)),
                        SelectionHighlightPoint(color = Color(0xFF2196F3)),
                        ShadowUnderLine(alpha = 0.3f, brush = null),
                        SelectionHighlightPopUp()
                    )
                )
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(),
            backgroundColor = Color.White
        )

        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = lineChartData
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Data list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(heartRateData.size) { index ->
                val data = heartRateData[index]
                DataItem(
                    timestamp = data.recordedAt,
                    values = "${data.heartRate} BPM",
                    category = data.category
                )
            }
        }
    }


    @Composable
    fun HistoricalDataCard(
        title: String,
        icon: ImageVector,
        isExpanded: Boolean,
        onToggleExpanded: () -> Unit,
        content: @Composable () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleExpanded() },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2196F3)
                        )
                    }

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = Color(0xFF2196F3)
                    )
                }

                if (isExpanded) {
                    Spacer(modifier = Modifier.height(16.dp))
                    content()
                }
            }
        }
    }
}

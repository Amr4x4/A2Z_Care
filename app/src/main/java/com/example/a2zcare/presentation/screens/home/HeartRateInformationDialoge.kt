package com.example.a2zcare.presentation.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a2zcare.presentation.theme.fieldCardColor

@Composable
fun HeartRateInformationDialog(
    isOpen: Boolean,
    title: String = "Blood Pressure Categories",
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onConfirmButtonClick) {
                    Text(text = "OK")
                }
            },
            containerColor = fieldCardColor,
            title = {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    item {
                        BloodPressureTable()
                    }
                }
            }
        )
    }
}

@Composable
fun BloodPressureTable() {
    val headerColor = Color.LightGray
    val cellTextColor = Color.Red

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TableRow("Category", "SBP", "DBP", header = true, textColor = headerColor)

        TableRow("Normal", "< 120  mmHg", "And < 80  mmHg", textColor = Color.Green)
        TableRow("Hypotension", "< 90    mmHg", "< 60    mmHg", textColor = cellTextColor)
        TableRow("Elevated", "120–129 mmHg", "And < 80 mmHg", textColor = cellTextColor)
        TableRow("High Blood Pressure Stage 1", "130–139 mmHg", "Or 80–89 mmHg", textColor = cellTextColor)
        TableRow("High Blood Pressure Stage 2", "≥ 140  mmHg", "Or ≥ 90  mmHg", textColor = cellTextColor)
        TableRow("Hypertensive Crisis", " > 180  mmHg", "And/or >120 mmHg", textColor = cellTextColor)
    }
}

@Composable
fun TableRow(
    label: String,
    sbp: String,
    dbp: String,
    header: Boolean = false,
    textColor: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = if (header) 6.dp else 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = if (header) 16.sp else 14.sp,
            fontWeight = if (header) FontWeight.Bold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = sbp,
            fontSize = if (header) 16.sp else 14.sp,
            fontWeight = if (header) FontWeight.Bold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = dbp,
            fontSize = if (header) 16.sp else 14.sp,
            fontWeight = if (header) FontWeight.Bold else FontWeight.Normal,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
    }
}

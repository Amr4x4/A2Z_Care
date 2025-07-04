package com.example.a2zcare.presentation.screens.home.onboarding_heart_home_card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a2zcare.presentation.theme.fieldCardColor
import java.util.Locale

@Composable
fun BloodPressureAdviceDialog(
    isOpen: Boolean,
    status: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    if (!isOpen) return

    val (adviceTitle, adviceList) = when (status.lowercase(Locale.ROOT)) {
        "hypertensionstage1", "high blood pressure stage 1" -> "Hypertension Stage 1" to listOf(
            "• Reduce salt intake",
            "• Exercise regularly (30 min/day)",
            "• Eat more fruits and vegetables",
            "• Limit alcohol and caffeine",
            "• Monitor your blood pressure"
        )
        "hypertensionstage2", "high blood pressure stage 2" -> "Hypertension Stage 2" to listOf(
            "• Consult your doctor for medication",
            "• Strictly reduce salt and processed foods",
            "• Avoid smoking and alcohol",
            "• Manage stress (yoga, meditation)",
            "• Follow a DASH diet"
        )
        "elevated" -> "Elevated Blood Pressure" to listOf(
            "• Maintain a healthy weight",
            "• Increase physical activity",
            "• Eat potassium-rich foods (bananas, spinach)",
            "• Reduce sodium intake",
            "• Monitor regularly"
        )
        "hypotension" -> "Low Blood Pressure (Hypotension)" to listOf(
            "• Drink more water",
            "• Eat small, frequent meals",
            "• Avoid sudden position changes",
            "• Add a little more salt (if advised by doctor)",
            "• Wear compression stockings"
        )
        "hypertensive crisis" -> "Hypertensive Crisis" to listOf(
            "• Seek emergency medical attention immediately!",
            "• Symptoms: severe headache, chest pain, shortness of breath",
            "• Do not wait—call emergency services"
        )
        else -> "Normal Blood Pressure" to listOf(
            "• Keep up the healthy lifestyle!",
            "• Continue regular exercise",
            "• Maintain a balanced diet",
            "• Monitor your blood pressure periodically"
        )
    }

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
                text = adviceTitle,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                adviceList.forEach {
                    Text(text = it, color = Color.White, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    )
}

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
fun HeartRateAdviceDialog(
    isOpen: Boolean,
    status: String,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    if (!isOpen) return

    val (adviceTitle, adviceList) = when (status.lowercase(Locale.ROOT)) {
        "tachycardia", "high" -> "High Heart Rate (Tachycardia)" to listOf(
            "• Practice deep breathing or meditation",
            "• Avoid caffeine and stimulants",
            "• Stay hydrated",
            "• Take a break and rest",
            "• Eat foods rich in potassium (bananas, oranges)",
            "• Consult your doctor if persistent"
        )
        "bradycardia", "low" -> "Low Heart Rate (Bradycardia)" to listOf(
            "• Gradually increase physical activity",
            "• Avoid sudden standing after sitting/lying",
            "• Eat small, frequent meals",
            "• Stay hydrated",
            "• Include foods with healthy fats (avocado, nuts)",
            "• Consult your doctor if you feel dizzy or faint"
        )
        else -> "Normal Heart Rate" to listOf(
            "• Maintain regular exercise",
            "• Eat a balanced diet",
            "• Manage stress",
            "• Stay hydrated",
            "• Get enough sleep"
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

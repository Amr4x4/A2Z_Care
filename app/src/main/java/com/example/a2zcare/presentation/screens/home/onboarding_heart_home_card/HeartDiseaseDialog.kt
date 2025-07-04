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
fun HeartDiseaseAdviceDialog(
    isOpen: Boolean,
    prediction: String,
    countPerMinute: Int,
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit
) {
    if (!isOpen) return

    val (adviceTitle, adviceList) = when (prediction) {
        "Supraventricular premature" -> "Supraventricular Premature" to listOf(
            "• Detected $countPerMinute beats/min",
            "• Reduce caffeine and alcohol",
            "• Manage stress (yoga, meditation)",
            "• Avoid smoking",
            "• Consult your doctor if symptoms persist"
        )
        "Premature ventricular contraction" -> "Premature Ventricular Contraction" to listOf(
            "• Detected $countPerMinute beats/min",
            "• Avoid stimulants (caffeine, tobacco)",
            "• Get enough sleep",
            "• Monitor your symptoms",
            "• Seek medical advice if frequent or symptomatic"
        )
        "Fusion of ventricular and normal" -> "Fusion of Ventricular and Normal" to listOf(
            "• Detected $countPerMinute beats/min",
            "• Maintain a healthy lifestyle",
            "• Avoid excessive stress",
            "• Regular checkups recommended"
        )
        "Unclassifiable" -> "Unclassifiable Rhythm" to listOf(
            "• Detected $countPerMinute beats/min",
            "• Ensure device is properly attached",
            "• If symptoms occur, consult a doctor"
        )
        else -> "Normal Heart Rhythm" to listOf(
            "• Keep up your healthy habits!",
            "• Continue regular exercise",
            "• Eat a balanced diet",
            "• Monitor your heart health periodically"
        )
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmButtonClick) {
                Text(text = "OK", color = Color.White, fontSize = 15.sp)
            }
        },
        containerColor = fieldCardColor,
        title = {
            Text(
                text = adviceTitle,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                adviceList.forEach {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    )
}

internal fun isHeartDiseaseNormal(prediction: String): Boolean {
    return prediction.lowercase(Locale.ROOT) == "normal"
}

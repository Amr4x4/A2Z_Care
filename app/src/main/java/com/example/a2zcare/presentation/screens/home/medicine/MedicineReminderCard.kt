package com.example.a2zcare.presentation.screens.home.medicine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.data.local.entity.MedicineType
import com.example.a2zcare.data.model.NextMedicineInfo
import com.example.a2zcare.presentation.viewmodel.MedicineViewModel
import java.util.concurrent.TimeUnit

@Composable
fun MedicineReminderCard(
    navController: NavController,
    viewModel: MedicineViewModel = hiltViewModel()
) {
    val nextMedicine by viewModel.nextMedicine.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("medicine_manager") },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F5E8)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "💊 Medicine Reminder",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )

                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "View All",
                    tint = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            nextMedicine?.let { medicineInfo ->
                NextMedicineDisplay(medicineInfo = medicineInfo)
            } ?: run {
                NoMedicineMessage()
            }
        }
    }
}

@Composable
fun NextMedicineDisplay(medicineInfo: NextMedicineInfo) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = medicineInfo.medicine.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2E7D32)
                )

                Text(
                    text = "Dose: ${medicineInfo.medicine.dose}",
                    fontSize = 14.sp,
                    color = Color(0xFF388E3C)
                )

                if (medicineInfo.medicine.type == MedicineType.PILLS) {
                    Text(
                        text = "Pills left: ${medicineInfo.medicine.remainingPills}",
                        fontSize = 14.sp,
                        color = if (medicineInfo.medicine.remainingPills <= 5)
                            Color.Red else Color(0xFF388E3C)
                    )
                }
            }

            Icon(
                imageVector = when (medicineInfo.medicine.type) {
                    MedicineType.PILLS -> Icons.Default.Medication
                    MedicineType.LIQUID -> Icons.Default.LocalDrink
                    MedicineType.INJECTION -> Icons.Default.Healing
                    MedicineType.DROPS -> Icons.Default.Opacity
                    MedicineType.INHALER -> Icons.Default.Air
                },
                contentDescription = medicineInfo.medicine.type.name,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFE8F5E8),
                    RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Next dose in:",
                    fontSize = 12.sp,
                    color = Color(0xFF2E7D32)
                )

                Text(
                    text = formatTimeRemaining(medicineInfo.remainingTimeMillis),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
            }

            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = "Time",
                tint = Color(0xFF4CAF50)
            )
        }

        if (medicineInfo.medicine.tips.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "💡 ${medicineInfo.medicine.tips}",
                fontSize = 12.sp,
                color = Color(0xFF4CAF50),
                modifier = Modifier
                    .background(
                        Color(0xFFE8F5E8),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Composable
fun NoMedicineMessage() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.MedicalServices,
            contentDescription = "No Medicine",
            tint = Color(0xFF81C784),
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "No medicines scheduled",
            fontSize = 16.sp,
            color = Color(0xFF4CAF50),
            textAlign = TextAlign.Center
        )

        Text(
            text = "Tap to add your first medicine",
            fontSize = 14.sp,
            color = Color(0xFF81C784),
            textAlign = TextAlign.Center
        )
    }
}

fun formatTimeRemaining(milliseconds: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds) % 60

    return when {
        hours > 0 -> "${hours}h ${minutes}m"
        minutes > 0 -> "${minutes}m"
        else -> "Now"
    }
}

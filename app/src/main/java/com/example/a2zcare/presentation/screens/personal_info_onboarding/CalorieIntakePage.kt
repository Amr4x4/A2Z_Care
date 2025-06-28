package com.example.a2zcare.presentation.screens.personal_info_onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.a2zcare.domain.entities.CalorieIntakeType
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.theme.navBarBackground

@Composable
fun CalorieIntakePage(
    selectedCalorieIntakeType: CalorieIntakeType?,
    onCalorieIntakeTypeSelected: (CalorieIntakeType) -> Unit,
    stepsTarget: Int,
    caloriesTarget: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Goal Type",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(15.dp))

        val goalDescriptions = mapOf(
            CalorieIntakeType.MAINTENANCE to "Maintain current weight",
            CalorieIntakeType.WEIGHT_LOSS to "Lose weight gradually",
            CalorieIntakeType.WEIGHT_GAIN to "Gain weight healthily",
            CalorieIntakeType.MUSCLE_BUILDING to "Build muscle mass"
        )

        CalorieIntakeType.values().forEach { type ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCalorieIntakeTypeSelected(type) },
                colors = CardDefaults.cardColors(
                    containerColor = navBarBackground
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedCalorieIntakeType == type,
                        onClick = { onCalorieIntakeTypeSelected(type) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.White,
                            unselectedColor = Color.LightGray
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = type.name.lowercase().split('_').joinToString(" ") {
                                it.replaceFirstChar { char -> char.uppercase() }
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = goalDescriptions[type] ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (stepsTarget > 0 && caloriesTarget > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = fieldCardColor
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your Daily Targets",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Steps: $stepsTarget",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Text(
                        text = "Calories to burn: $caloriesTarget",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                }
            }
        }
    }
}

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
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.theme.navBarBackground

@Composable
fun HealthGoalsPage(
    selectedHealthGoals: String?,
    onHealthGoalsSelected: (String) -> Unit,
    stepsTarget: Int,
    caloriesTarget: Int,
    caloriesConsumed: Int = 0
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
            text = "Health Goals",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(15.dp))

        val healthGoals = mapOf(
            "Lose Weight" to "Lose weight gradually and safely",
            "Gain Weight" to "Gain weight in a healthy way",
            "Build Muscle" to "Build muscle mass and strength",
            "Maintain Current Weight" to "Maintain your current weight"
        )

        healthGoals.forEach { (goal, description) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onHealthGoalsSelected(goal) },
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
                        selected = selectedHealthGoals == goal,
                        onClick = { onHealthGoalsSelected(goal) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.White,
                            unselectedColor = Color.LightGray
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = goal,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (stepsTarget > 0 && caloriesTarget > 0) {
            Spacer(modifier = Modifier.height(16.dp))
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
                        text = "Calories to gain: $caloriesTarget",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    if (caloriesConsumed > caloriesTarget) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "You exceeded your daily target by ${caloriesConsumed - caloriesTarget} cal. You need to burn this much to return to your target.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                }
            }
        }
    }
}
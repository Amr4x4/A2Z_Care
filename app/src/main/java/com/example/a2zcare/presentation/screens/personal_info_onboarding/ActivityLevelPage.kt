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
import com.example.a2zcare.domain.entities.ActivityLevel
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.navBarBackground


@Composable
fun ActivityLevelPage(
    selectedActivityLevel: ActivityLevel?,
    onActivityLevelSelected: (ActivityLevel) -> Unit
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
            text = "Activity Level",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(15.dp))

        val activityDescriptions = mapOf(
            ActivityLevel.SEDENTARY to "Little to no exercise",
            ActivityLevel.LIGHTLY_ACTIVE to "Light exercise 1-3 days/week",
            ActivityLevel.MODERATELY_ACTIVE to "Moderate exercise 3-5 days/week",
            ActivityLevel.VERY_ACTIVE to "Hard exercise 6-7 days/week",
            ActivityLevel.EXTREMELY_ACTIVE to "Very hard exercise, training twice/day"
        )

        ActivityLevel.values().forEach { level ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onActivityLevelSelected(level) },
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
                        selected = selectedActivityLevel == level,
                        onClick = { onActivityLevelSelected(level) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color.White,
                            unselectedColor = Color.LightGray
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = level.name.lowercase().split('_').joinToString(" ") {
                                it.replaceFirstChar { char -> char.uppercase() }
                            },
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = activityDescriptions[level] ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.LightGray
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

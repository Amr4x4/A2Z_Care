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
import com.example.a2zcare.presentation.theme.navBarBackground

@Composable
fun ActivityLevelPage(
    selectedActivityLevel: String?,
    onActivityLevelSelected: (String) -> Unit
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

        val activityLevels = mapOf(
            "Sedentary" to "Little to no exercise",
            "Lightly Active" to "Light exercise 1-3 days/week",
            "Moderately Active" to "Moderate exercise 3-5 days/week",
            "Very Active" to "Hard exercise 6-7 days/week",
            "Extremely Active" to "Very hard exercise, training twice/day"
        )

        activityLevels.forEach { (level, description) ->
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
                            text = level,
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
    }
}
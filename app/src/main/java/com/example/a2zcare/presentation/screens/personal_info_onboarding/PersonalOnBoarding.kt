package com.example.a2zcare.presentation.screens.personal_info_onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.domain.entities.ActivityLevel
import com.example.a2zcare.domain.entities.CalorieIntakeType
import com.example.a2zcare.domain.entities.Gender
import com.example.a2zcare.presentation.viewmodel.PersonalOnboardingViewModel
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalOnboardingScreen(
    navController: NavController,
    viewModel: PersonalOnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.isCompleted) {
        if (uiState.isCompleted) {
            navController.navigate("main_route") {
                popUpTo("onboarding_screen") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Progress indicator
        LinearProgressIndicator(
            progress = { (pagerState.currentPage + 1) / 5f },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
        )

        HorizontalPager(
            count = 5,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                0 -> WelcomePage()
                1 -> PersonalInfoPage(
                    age = uiState.age,
                    height = uiState.height,
                    weight = uiState.weight,
                    onAgeChange = viewModel::updateAge,
                    onHeightChange = viewModel::updateHeight,
                    onWeightChange = viewModel::updateWeight
                )
                2 -> GenderPage(
                    selectedGender = uiState.gender,
                    onGenderSelected = viewModel::updateGender
                )
                3 -> ActivityLevelPage(
                    selectedActivityLevel = uiState.activityLevel,
                    onActivityLevelSelected = viewModel::updateActivityLevel
                )
                4 -> CalorieIntakePage(
                    selectedCalorieIntakeType = uiState.calorieIntakeType,
                    onCalorieIntakeTypeSelected = viewModel::updateCalorieIntakeType,
                    stepsTarget = uiState.calculatedStepsTarget,
                    caloriesTarget = uiState.calculatedCaloriesTarget
                )
            }
        }

        // Navigation buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (pagerState.currentPage > 0) {
                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                ) {
                    Text("Previous")
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < 4) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        viewModel.saveUserProfile()
                    }
                },
                enabled = when (pagerState.currentPage) {
                    1 -> uiState.age > 0 && uiState.height > 0 && uiState.weight > 0
                    2 -> uiState.gender != null
                    3 -> uiState.activityLevel != null
                    4 -> uiState.calorieIntakeType != null
                    else -> true
                }
            ) {
                Text(if (pagerState.currentPage < 4) "Next" else "Complete Setup")
            }
        }
    }
}


@Composable
private fun WelcomePage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.DirectionsWalk,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Welcome to Step Tracker",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Let's set up your personalized fitness profile to help you reach your daily goals",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PersonalInfoPage(
    age: Int,
    height: Float,
    weight: Float,
    onAgeChange: (Int) -> Unit,
    onHeightChange: (Float) -> Unit,
    onWeightChange: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Personal Information",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = if (age > 0) age.toString() else "",
            onValueChange = {
                it.toIntOrNull()?.let { age -> onAgeChange(age) }
            },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = if (height > 0) height.toString() else "",
            onValueChange = {
                it.toFloatOrNull()?.let { height -> onHeightChange(height) }
            },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = if (weight > 0) weight.toString() else "",
            onValueChange = {
                it.toFloatOrNull()?.let { weight -> onWeightChange(weight) }
            },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun GenderPage(
    selectedGender: Gender?,
    onGenderSelected: (Gender) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Gender",
            style = MaterialTheme.typography.headlineMedium
        )

        Gender.values().forEach { gender ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onGenderSelected(gender) },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedGender == gender)
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedGender == gender,
                        onClick = { onGenderSelected(gender) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = gender.name.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun ActivityLevelPage(
    selectedActivityLevel: ActivityLevel?,
    onActivityLevelSelected: (ActivityLevel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Activity Level",
            style = MaterialTheme.typography.headlineMedium
        )

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
                    containerColor = if (selectedActivityLevel == level)
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedActivityLevel == level,
                        onClick = { onActivityLevelSelected(level) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = level.name.lowercase().split('_').joinToString(" ") {
                                it.replaceFirstChar { char -> char.uppercase() }
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = activityDescriptions[level] ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalorieIntakePage(
    selectedCalorieIntakeType: CalorieIntakeType?,
    onCalorieIntakeTypeSelected: (CalorieIntakeType) -> Unit,
    stepsTarget: Int,
    caloriesTarget: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Goal Type",
            style = MaterialTheme.typography.headlineMedium
        )

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
                    containerColor = if (selectedCalorieIntakeType == type)
                        MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surface
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedCalorieIntakeType == type,
                        onClick = { onCalorieIntakeTypeSelected(type) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = type.name.lowercase().split('_').joinToString(" ") {
                                it.replaceFirstChar { char -> char.uppercase() }
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = goalDescriptions[type] ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (stepsTarget > 0 && caloriesTarget > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Your Daily Targets",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Steps: $stepsTarget",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Calories to burn: $caloriesTarget",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
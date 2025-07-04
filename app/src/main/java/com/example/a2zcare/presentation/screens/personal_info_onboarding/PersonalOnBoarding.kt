package com.example.a2zcare.presentation.screens.personal_info_onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.selected
import com.example.a2zcare.presentation.theme.unselected
import com.example.a2zcare.presentation.viewmodel.CalorieViewModel
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
    val calorieViewModel: CalorieViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    // Track if file upload or skip is done
    val fileUploadOrSkipDone = remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isCompleted) {
        if (uiState.isCompleted) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.PersonalOnBoarding.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(uiState.calculatedCaloriesTarget) {
        if (uiState.calculatedCaloriesTarget > 0) {
            calorieViewModel.applyCalculatedCaloriesTarget(uiState.calculatedCaloriesTarget)
        }
    }

    uiState.errorMessage?.let { errorMessage ->
        AlertDialog(
            onDismissRequest = { viewModel.clearError() },
            title = {
                Text(
                    text = "Error",
                    color = Color.White
                )
            },
            text = {
                Text(
                    text = errorMessage,
                    color = Color.White
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.clearError() }
                ) {
                    Text("OK", color = Color.White)
                }
            },
            containerColor = backgroundColor
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(horizontal = 12.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                progress = { (pagerState.currentPage + 1) / 7f },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                color = Color.Red,
                trackColor = Color.DarkGray
            )

            HorizontalPager(
                count = 7,
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                when (page) {
                    0 -> WelcomePage()
                    1 -> BasicInfoPage(
                        firstName = uiState.firstName,
                        lastName = uiState.lastName,
                        phoneNumber = uiState.phoneNumber,
                        onFirstNameChange = viewModel::updateFirstName,
                        onLastNameChange = viewModel::updateLastName,
                        onPhoneNumberChange = viewModel::updatePhoneNumber
                    )
                    2 -> PersonalInfoPage(
                        age = uiState.age,
                        height = uiState.height,
                        weight = uiState.weight,
                        onAgeChange = viewModel::updateAge,
                        onHeightChange = viewModel::updateHeight,
                        onWeightChange = viewModel::updateWeight
                    )
                    3 -> GenderPage(
                        selectedGender = uiState.gender,
                        onGenderSelected = viewModel::updateGender
                    )
                    4 -> ActivityLevelPage(
                        selectedActivityLevel = uiState.activityLevel, // FIXED: Now using correct field
                        onActivityLevelSelected = viewModel::updateActivityLevel
                    )
                    5 -> HealthGoalsPage(
                        selectedHealthGoals = uiState.healthGoals,
                        onHealthGoalsSelected = viewModel::updateHealthGoals,
                        stepsTarget = uiState.calculatedStepsTarget,
                        caloriesTarget = uiState.calculatedCaloriesTarget
                    )
                    6 -> SensorDataScreen(
                        onUploadOrSkip = { fileUploadOrSkipDone.value = true }
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    color = Color.DarkGray
                )
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
                            },
                            enabled = !uiState.isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = selected,
                                contentColor = Color.White,
                                disabledContainerColor = unselected,
                                disabledContentColor = Color.White.copy(alpha = 0.6f)
                            )
                        ) {
                            Text("Previous")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Button(
                        onClick = {
                            if (pagerState.currentPage < 6) {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                }
                            } else {
                                viewModel.saveUserProfile()
                            }
                        },
                        enabled = !uiState.isLoading && when (pagerState.currentPage) {
                            1 -> uiState.firstName.isNotBlank() && uiState.lastName.isNotBlank()
                            2 -> uiState.age > 0 && uiState.height > 0 && uiState.weight > 0
                            3 -> !uiState.gender.isNullOrEmpty()
                            4 -> !uiState.activityLevel.isNullOrEmpty()
                            5 -> !uiState.healthGoals.isNullOrEmpty()
                            6 -> fileUploadOrSkipDone.value // Only enable if file uploaded or skipped
                            else -> true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = selected,
                            contentColor = Color.White,
                            disabledContainerColor = unselected,
                            disabledContentColor = Color.White.copy(alpha = 0.6f)
                        )
                    ) {
                        if (uiState.isLoading && pagerState.currentPage == 6) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Saving...")
                            }
                        } else {
                            Text(if (pagerState.currentPage < 6) "Next" else "Complete Setup")
                        }
                    }
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Updating your profile...",
                        color = Color.White
                    )
                }
            }
        }
    }
}
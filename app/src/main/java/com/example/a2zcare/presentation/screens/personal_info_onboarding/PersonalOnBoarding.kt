package com.example.a2zcare.presentation.screens.personal_info_onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.PersonalOnBoarding.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(
            progress = { (pagerState.currentPage + 1) / 5f },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            color = Color.Red,
            trackColor = Color.DarkGray
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
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = selected,
                        contentColor = Color.White,
                        disabledContainerColor = unselected,
                        disabledContentColor = Color.White.copy(alpha = 0.6f)
                    )
                ) {
                    Text(if (pagerState.currentPage < 4) "Next" else "Complete Setup")
                }
            }
        }
    }
}
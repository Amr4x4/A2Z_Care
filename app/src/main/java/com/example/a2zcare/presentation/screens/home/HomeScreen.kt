package com.example.a2zcare.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.presentation.navegation.bottomNavItems
import com.example.a2zcare.presentation.screens.home.onboarding_heart_home_card.BloodPressurePredictionCard
import com.example.a2zcare.presentation.screens.home.onboarding_heart_home_card.HeartDiseasePredictionCard
import com.example.a2zcare.presentation.screens.home.onboarding_heart_home_card.HeartRateCard
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.viewmodel.HealthDataViewModel
import com.example.a2zcare.presentation.viewmodel.UserViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val firstName by userViewModel.firstName.collectAsState()
    val pagerState = rememberPagerState(initialPage = 0)
    val coroutineScope = rememberCoroutineScope()
    val healthDataViewModel: HealthDataViewModel = hiltViewModel()
    val healthUiState by healthDataViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            HomeTopBar(
                username = firstName,
                onBackButtonClick = { /* Handle pro upgrade */ },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                items = bottomNavItems,
                onItemSelected = { index ->
                    navController.navigate(bottomNavItems[index].route) {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(innerPadding)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.Start
                ) {
                    HorizontalPager(
                        count = 3,
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(340.dp)
                    ) { page ->
                        when (page) {
                            0 -> BloodPressurePredictionCard(bloodPressureData = healthUiState.bloodPressureData)
                            1 -> HeartRateCard(
                                viewModel = healthDataViewModel,
                                modifier = Modifier,
                            )
                            2 -> HeartDiseasePredictionCard(
                                viewModel = healthDataViewModel,
                                modifier = Modifier
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(3) { index ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 12.dp else 8.dp)
                                    .background(
                                        color = if (isSelected) Color.LightGray else Color.DarkGray,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                                    }
                            )
                            if (index < 2) Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StepsCard(
                            modifier = Modifier.weight(1f),
                            navController = navController
                        )
                        WaterTrackingCard(
                            modifier = Modifier.weight(1f),
                            navController
                        )
                    }
                    CaloriesProgressCard(
                        currentCalories = 500,
                        targetCalories = 1200
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    HomeScreen(navController = rememberNavController())
}
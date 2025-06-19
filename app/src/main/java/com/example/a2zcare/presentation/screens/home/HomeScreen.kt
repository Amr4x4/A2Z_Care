package com.example.a2zcare.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Notification.route,
        Screen.VIP.route,
        Screen.Tracker.route,
        Screen.Profile.route
    )

    Scaffold(
        topBar = {
            HomeTopBar(
                userName = "Amr",
                onBackButtonClick = { },
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                onItemSelected = { index ->
                    selectedIndex = index
                    navController.navigate(bottomNavRoutes[index]) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .padding(innerPadding)
        ){
            item {

            }
            item {

            }
            item {

            }

        }

    }
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    HomeScreen( navController = rememberNavController())
}
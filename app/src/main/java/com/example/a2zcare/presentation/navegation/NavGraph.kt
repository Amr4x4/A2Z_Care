package com.example.onboardingcompose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.onboarding.OnBoardingNav
import com.example.a2zcare.presentation.screens.get_start.GetStart
import com.example.a2zcare.presentation.screens.log_in.LogInScreen
import com.example.a2zcare.presentation.screens.sign_up.SignUpScreen
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalAnimationApi
@ExperimentalPagerApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Screen.OnBoarding.route) {
            OnBoardingNav(navController = navController)
        }
        composable(route = Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }
        composable(route = Screen.GetStart.route) {
            GetStart()
        }
        composable(route = Screen.LogIn.route) {
            LogInScreen(navController = navController)
        }
    }
}
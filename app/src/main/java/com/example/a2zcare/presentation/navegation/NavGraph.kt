package com.example.a2zcare.presentation.navegation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a2zcare.presentation.onboarding.OnBoardingNav
import com.example.a2zcare.presentation.screens.get_start.GetStart
import com.example.a2zcare.presentation.screens.home.HomeScreen
import com.example.a2zcare.presentation.screens.log_in.LogInScreen
import com.example.a2zcare.presentation.screens.notification.NotificationScreen
import com.example.a2zcare.presentation.screens.personal_info_onboarding.PersonalOnboardingScreen
import com.example.a2zcare.presentation.screens.profile.ProfileScreen
import com.example.a2zcare.presentation.screens.sign_up.SignUpScreen
import com.example.a2zcare.presentation.screens.tracker.HomeScreenVivo
import com.example.a2zcare.presentation.screens.tracker.LocationSharingScreen
import com.example.a2zcare.presentation.screens.tracker.NotificationScreenVivo
import com.example.a2zcare.presentation.screens.tracker.TrackerScreen
import com.example.a2zcare.presentation.screens.vip.VIPScreen
import com.example.a2zcare.presentation.screens.water_tracking.WaterTrackingScreen
import com.example.a2zcare.presentation.screens.tracker.StepsTrackerScreen
import com.example.a2zcare.presentation.screens.steps_tracker.StepsTrackingScreen
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
            GetStart(navController = navController)
        }
        composable(route = Screen.LogIn.route) {
            LogInScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Notification.route) {
            NotificationScreen(navController = navController)
        }
        composable(route = Screen.VIP.route) {
            VIPScreen(navController = navController)
        }
        composable(route = Screen.WaterTracker.route) {
            WaterTrackingScreen(navController = navController)
        }
        composable(route = Screen.PersonalOnBoarding.route) {
            PersonalOnboardingScreen(navController = navController)
        }
        composable(route = Screen.StepsTracking.route) {
            StepsTrackingScreen(navController = navController)
        }
        composable(route = Screen.StepsTracker.route) {
            StepsTrackerScreen(navController = navController)
        }
        composable(route = Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }


        //
        composable(route = Screen.Tracker.route) {
            TrackerScreen(navController = navController)
        }
        composable(Screen.HomeVivo.route) {
            HomeScreenVivo(
                onNavigateToRun = {
                    navController.navigate(Screen.Run.route)
                },
                onNavigateToLocationSharing = {
                    navController.navigate(Screen.LocationSharing.route)
                }
            )
        }



        composable(Screen.LocationSharing.route) {
            LocationSharingScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Notifications.route) {
            NotificationScreenVivo(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
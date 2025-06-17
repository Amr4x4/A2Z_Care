package com.example.a2zcare.presentation.navegation

sealed class Screen(val route: String) {
    object OnBoarding : Screen(route = "onboarding")
    object SignUp : Screen(route = "sign_up")
    object GetStart : Screen(route = "get_start")
    object LogIn : Screen(route = "log_in")
    object Home : Screen(route = "home")
    object Notification : Screen(route = "notification")
    object VIP : Screen(route = "vip")
    object Tracker : Screen(route = "tracker")
    object Profile : Screen(route = "profile")
}
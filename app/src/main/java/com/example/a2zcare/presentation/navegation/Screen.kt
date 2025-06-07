package com.example.a2zcare.presentation.navegation

sealed class Screen(val route: String) {
    object OnBoarding : Screen(route = "onboarding")
    object SignUp : Screen(route = "sign_up")
    object GetStart : Screen(route = "get_start")
    object LogIn : Screen(route = "log_in")

}
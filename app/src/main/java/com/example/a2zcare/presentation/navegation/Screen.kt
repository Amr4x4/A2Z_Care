package com.example.a2zcare.presentation.navegation

sealed class Screen(val route: String) {
    object OnBoarding : Screen(route = "onboarding")
    object SignUp : Screen(route = "sign_up")
    object Welcome : Screen(route = "welcome")
    object GetStart : Screen(route = "get_start")
    object LogIn : Screen(route = "log_in")
    object Home : Screen(route = "home")
    object Notification : Screen(route = "notification")
    object VIP : Screen(route = "vip")
    object Tracker : Screen(route = "tracker")
    object WaterTracker : Screen(route = "water_tracker")
    object StepsTracker : Screen(route = "steps_tracker")
    object PersonalOnBoarding : Screen(route = "personal_onboarding")
    object Profile : Screen(route = "profile")
    object StepsTracking : Screen(route = "steps_tracking")



    object Gender : Screen(route = "gender")
    object Birthday : Screen(route = "birthday")
    object Weight : Screen(route = "weight")
    object Height : Screen(route = "height")
    object Reminders : Screen(route = "reminders")
    object Loading : Screen(route = "loading")

    //
    object HomeVivo : Screen("home_vivo")
    object Run : Screen("run")
    object LocationSharing : Screen("location_sharing")
    object Notifications : Screen("notifications")
}
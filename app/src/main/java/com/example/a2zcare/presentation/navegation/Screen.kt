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
    object WaterTracker : Screen(route = "water_tracker")
    object RunSession : Screen(route = "run_session")
    object PersonalOnBoarding : Screen(route = "personal_onboarding")
    object Profile : Screen(route = "profile")
    object StepsTracking : Screen(route = "steps_tracking")
    object LocationSharing : Screen("location_sharing")
    object SelectEmail : Screen("select_email")
    object OPTSentSuccessfullyCheckEmail : Screen("opt_sent_successfully_check_email")
    object PersonalInformationScreen : Screen("personal_information_screen")
    object NotificationSittingScreen : Screen("notification_sitting_screen")
    object SecuritySettingScreen : Screen("security_setting_screen")
    object ChangePassword : Screen("change_password")

    object PaymentMethodsScreen : Screen("payment_methods_screen")
    object SubscriptionsScreen : Screen("subscriptions_screen")
    object HelpSupportScreen : Screen("help_support_screen")
    object PrivacyPolicyScreen : Screen("privacy_policy_screen")
    object AboutScreen : Screen("about_screen")


    object CaloriesScreen : Screen("calories_screen")
    object VIPMedicineScreen : Screen("vip_medicine_screen")

}
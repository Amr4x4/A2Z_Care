package com.example.a2zcare.presentation.navegation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.a2zcare.presentation.onboarding.OnBoardingNav
import com.example.a2zcare.presentation.screens.calories.CaloriesScreen
import com.example.a2zcare.presentation.screens.get_start.GetStart
import com.example.a2zcare.presentation.screens.home.HomeScreen
import com.example.a2zcare.presentation.screens.home.medicine.MedicineManagerScreen
import com.example.a2zcare.presentation.screens.log_in.LogInScreen
import com.example.a2zcare.presentation.screens.log_in.forget_password.OPTSentSuccessfullyCheckEmail
import com.example.a2zcare.presentation.screens.log_in.forget_password.SelectEmailScreen
import com.example.a2zcare.presentation.screens.notification.NotificationScreen
import com.example.a2zcare.presentation.screens.personal_info_onboarding.PersonalOnboardingScreen
import com.example.a2zcare.presentation.screens.profile.AboutScreen
import com.example.a2zcare.presentation.screens.profile.ChangePasswordScreen
import com.example.a2zcare.presentation.screens.profile.HelpSupportScreen
import com.example.a2zcare.presentation.screens.profile.NotificationSettingsScreen
import com.example.a2zcare.presentation.screens.profile.PaymentMethodsScreen
import com.example.a2zcare.presentation.screens.profile.PersonalInfoScreen
import com.example.a2zcare.presentation.screens.profile.PrivacyPolicyScreen
import com.example.a2zcare.presentation.screens.profile.ProfileScreen
import com.example.a2zcare.presentation.screens.profile.SecuritySettingsScreen
import com.example.a2zcare.presentation.screens.profile.SubscriptionsScreen
import com.example.a2zcare.presentation.screens.sign_up.SignUpScreen
import com.example.a2zcare.presentation.screens.steps_tracker.StepsTrackingScreen
import com.example.a2zcare.presentation.screens.tracker.LocationSharingScreen
import com.example.a2zcare.presentation.screens.steps_tracker.RunningSession
import com.example.a2zcare.presentation.screens.tracker.EmergencyContactScreen
import com.example.a2zcare.presentation.screens.tracker.HistoricalDataScreen
import com.example.a2zcare.presentation.screens.tracker.TrackerScreen
import com.example.a2zcare.presentation.screens.vip.ChatScreen
import com.example.a2zcare.presentation.screens.vip.ConsultationChatScreen
import com.example.a2zcare.presentation.screens.vip.VIPDoctorConsultationsScreen
import com.example.a2zcare.presentation.screens.vip.VIPHealthRecordsScreen
import com.example.a2zcare.presentation.screens.vip.VIPMedicineOrdersScreen
import com.example.a2zcare.presentation.screens.vip.VIPScreen
import com.example.a2zcare.presentation.screens.water_tracking.WaterTrackingScreen
import com.example.a2zcare.presentation.viewmodel.MainNavigationViewModel
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String,
    mainViewModel: MainNavigationViewModel = hiltViewModel()
) {
    val navigationState by mainViewModel.navigationState.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.checkInitialDestination()
    }

    navigationState.initialDestination?.let { startDestination ->
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
            composable(route = Screen.RunSession.route) {
                RunningSession(navController = navController)
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable(route = Screen.LocationSharing.route) {
                LocationSharingScreen(navController = navController)
            }
            composable(route = Screen.Tracker.route) {
                TrackerScreen(navController = navController)
            }
            composable(route = Screen.SelectEmail.route) {
                SelectEmailScreen(navController = navController)
            }
            composable(route = Screen.OPTSentSuccessfullyCheckEmail.route) {
                OPTSentSuccessfullyCheckEmail(navController = navController)
            }
            composable(route = Screen.PersonalInformationScreen.route) {
                PersonalInfoScreen(navController = navController)
            }
            composable(route = Screen.NotificationSittingScreen.route) {
                NotificationSettingsScreen(navController = navController)
            }
            composable(route = Screen.SecuritySettingScreen.route) {
                SecuritySettingsScreen(navController = navController)
            }
            composable(route = Screen.ChangePassword.route) {
                ChangePasswordScreen(navController = navController)
            }
            composable(route = Screen.PaymentMethodsScreen.route) {
                PaymentMethodsScreen(navController = navController)
            }
            composable(route = Screen.SubscriptionsScreen.route) {
                SubscriptionsScreen(navController = navController)
            }
            composable(route = Screen.HelpSupportScreen.route) {
                HelpSupportScreen(navController = navController)
            }
            composable(route = Screen.PrivacyPolicyScreen.route) {
                PrivacyPolicyScreen(navController = navController)
            }
            composable(route = Screen.AboutScreen.route) {
                AboutScreen(navController = navController)
            }
            composable(route = Screen.CaloriesScreen.route) {
                CaloriesScreen(navController = navController)
            }

            composable(route = Screen.ChatScreen.route) {
                ChatScreen(navController = navController)
            }
            composable(route = Screen.VIPMedicineScreen.route) {
                VIPMedicineOrdersScreen(navController = navController)
            }
            composable(route = Screen.VIPHealthRecordsScreen.route) {
                VIPHealthRecordsScreen(navController = navController)
            }
            composable(route = Screen.ConsultingChatScreen.route) {
                ConsultationChatScreen(navController = navController)
            }
            composable(route = Screen.EmergencyContactScreen.route) {
                EmergencyContactScreen(navController = navController)
            }
            composable(route = Screen.VIPDoctorConsultationsScreen.route) {
                VIPDoctorConsultationsScreen(navController = navController)
            }
            composable(route = Screen.MedicineManager.route) {
                MedicineManagerScreen(navController = navController)
            }
            composable(route = Screen.HistoricalData.route) {
                HistoricalDataScreen(navController = navController)
            }

        }
    }
}

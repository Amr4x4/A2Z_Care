package com.example.a2zcare.presentation.onboarding

import androidx.annotation.DrawableRes
import com.example.a2zcare.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val description: String
) {
    object First : OnBoardingPage(
        image = R.drawable.onboarding_first_image,
        title = "Welcome to A2Z Healthcare",
        description = "Your personal AI-powered healthcare companion, ready to assist you anytime, anywhere made by Azhar Group."
    )

    object Second : OnBoardingPage(
        image = R.drawable.onboarding_second_image,
        title = "Features",
        description = "Step Tracker, Calories Tracking, Water Intake Tracker, Medication Reminder, Medication History, Blood Pressure Prediction (AI-powered), " +
                "Accident Alert (sends location and alert to emergency contacts)"
    )

    object Third : OnBoardingPage(
        image = R.drawable.onboarding_third_image,
        title = "VIP Features",
        description = "Ability to order medicine directly via the app with faster delivery and exclusive service, " +
                " Request doctor consultations remotely through the app."
    )
}
package com.example.a2zcare.presentation.navegation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.a2zcare.data.repository.BottomNavItem

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, "home"),
    BottomNavItem("Notification", Icons.Default.Notifications, "notification"),
    BottomNavItem("VIP", Icons.Default.Star, "vip"),
    BottomNavItem("Tracker", Icons.Default.LocationOn, "tracker"),
    BottomNavItem("Profile", Icons.Default.Person, "profile")
)

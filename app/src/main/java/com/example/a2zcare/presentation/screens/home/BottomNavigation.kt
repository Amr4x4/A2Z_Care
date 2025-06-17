package com.example.a2zcare.presentation.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationImportant
import com.example.a2zcare.presentation.model.BottomNavDataClass

val items: List<BottomNavDataClass> = listOf(
    BottomNavDataClass(
        title = "Home",
        icon = Icons.Default.Home
    ),
    BottomNavDataClass(
        title = "Notification",
        icon = Icons.Default.NotificationImportant
    ),
    BottomNavDataClass(
        title = "VIP",
        icon = Icons.Default.LocalPolice
    ),
    BottomNavDataClass(
        title = "Tracker",
        icon = Icons.Default.MyLocation
    ),
    BottomNavDataClass(
        title = "Profile",
        icon = Icons.Default.AccountCircle
    )
)

package com.example.a2zcare.presentation.screens.tracker

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.navegation.bottomNavItems
import com.example.a2zcare.presentation.screens.home.BottomNavigationBar
import com.example.a2zcare.presentation.theme.backgroundColor
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TrackerScreen(navController: NavController) {
    val context = LocalContext.current
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val selectedIndex = bottomNavItems.indexOfFirst { it.route == currentDestination?.route }
        .takeIf { it >= 0 } ?: 3

    // Handle location permissions
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    Scaffold(
        topBar = {},
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                items = bottomNavItems,
                onItemSelected = { index ->
                    navController.navigate(bottomNavItems[index].route) {
                        popUpTo("tracker") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(padding)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tracker Screen", modifier = Modifier.padding(16.dp))

                    when {
                        locationPermissions.allPermissionsGranted -> {
                            // Show button to navigate to location sharing
                            Button(
                                onClick = {
                                    navController.navigate(Screen.LocationSharing.route)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text("Go to Location Sharing")
                            }
                        }

                        locationPermissions.shouldShowRationale -> {
                            PermissionRationaleCard(
                                onRequestPermissions = {
                                    locationPermissions.launchMultiplePermissionRequest()
                                }
                            )
                        }

                        else -> {
                            LaunchedEffect(Unit) {
                                locationPermissions.launchMultiplePermissionRequest()
                            }
                            PermissionRequestCard()
                        }

                    }
                    Button(
                        onClick = { navController.navigate(Screen.ChatScreen.route) }
                    ) {
                        Text(
                            text = "Go to VIP Medicine Orders Screen"
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun PermissionRationaleCard(
    onRequestPermissions: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Location Permission Required",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This feature requires location access to share your location with others.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRequestPermissions) {
                Text("Grant Permission")
            }
        }
    }
}
@Composable
private fun EmergencySection(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { navController.navigate("vip_emergency_services_screen") },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFF5252).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocalHospital,
                contentDescription = null,
                tint = Color(0xFFFF5252),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "Emergency Services",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF5252)
                )
                Text(
                    "24/7 emergency support available",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}
@Composable
private fun PermissionRequestCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Requesting location permission...",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
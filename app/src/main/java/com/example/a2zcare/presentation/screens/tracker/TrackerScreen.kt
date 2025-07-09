package com.example.a2zcare.presentation.screens.tracker

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataThresholding
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a2zcare.R
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.navegation.bottomNavItems
import com.example.a2zcare.presentation.screens.home.BottomNavigationBar
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.theme.fieldColor
import com.example.a2zcare.presentation.theme.selected
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(navController: NavController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val selectedIndex = bottomNavItems.indexOfFirst { it.route == currentDestination?.route }
        .takeIf { it >= 0 } ?: 3

    val locationPermissions = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "🧭 Tracking",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = fieldColor,
                    scrolledContainerColor = fieldColor
                )
            )
        },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                when {
                    locationPermissions.allPermissionsGranted -> {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            TrackerCard(
                                title = "Location Tracking",
                                imageRes = R.drawable.location_tracking,
                                icon = Icons.Default.LocationOn,
                                buttonText = "Go to Location Sharing",
                                onClick = { navController.navigate(Screen.LocationSharing.route) }
                            )
                            TrackerCard(
                                title = "Previous Data Tracking",
                                imageRes = R.drawable.user_data_analysis,
                                icon = Icons.Default.DataThresholding,
                                buttonText = "Go to Previous Data Tracking",
                                onClick = { navController.navigate(Screen.HistoricalData.route) }
                            )
                            TrackerCard(
                                title = "Emergency Tracking Settings",
                                imageRes = R.drawable.emergency,
                                icon = Icons.Default.Emergency,
                                buttonText = "Go to Emergency Tracking Settings",
                                onClick = { navController.navigate(Screen.VIPEmergencyServicesScreen.route) }
                            )
                        }
                    }

                    locationPermissions.shouldShowRationale -> {
                        PermissionRationaleCard {
                            locationPermissions.launchMultiplePermissionRequest()
                        }
                    }

                    else -> {
                        LaunchedEffect(Unit) {
                            locationPermissions.launchMultiplePermissionRequest()
                        }
                        PermissionRequestCard()
                    }
                }
            }
        }
    }
}

@Composable
fun TrackerCard(
    title: String,
    imageRes: Int,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = fieldCardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = selected,
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = buttonText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
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
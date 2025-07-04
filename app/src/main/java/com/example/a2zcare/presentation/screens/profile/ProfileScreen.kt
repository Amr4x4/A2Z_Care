package com.example.a2zcare.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.a2zcare.presentation.navegation.bottomNavItems
import com.example.a2zcare.presentation.screens.home.BottomNavigationBar
import com.example.a2zcare.presentation.screens.vip_payment.VIPCard
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val selectedIndex = bottomNavItems.indexOfFirst { it.route == currentDestination?.route }
        .takeIf { it >= 0 } ?: 4

    val user by viewModel.user.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideDeleteDialog() },
            title = { Text("Delete Account") },
            text = {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "This action is irreversible!",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text("Are you sure you want to delete your account? This will permanently remove all your data and cannot be undone.")
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.deleteAccount() }
                ) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.hideDeleteDialog() }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Scaffold(
        topBar = {
            ProfileTopBar(
                navController = navController,
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                items = bottomNavItems,
                onItemSelected = { index ->
                    navController.navigate(bottomNavItems[index].route) {
                        popUpTo("profile") { inclusive = false }
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
                VIPCard(navController)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    // Upgrade Banner
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE91E63)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Upgrade Plan Now!",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Enjoy all the benefits and explore more possibilities",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    // User Profile
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { navController.navigate("personal_info") }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE91E63)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "MG",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    user.userName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    user.email,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                            Icon(
                                Icons.Default.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }
                    }

                    // Menu Items
                    val menuItems = listOf(
                        MenuItemData("Preferences", Icons.Default.Settings, "preferences"),
                        MenuItemData("Reminder", Icons.Default.Notifications, "reminder"),
                        MenuItemData(
                            "Account & Security",
                            Icons.Default.Security,
                            "account_security"
                        ),
                        MenuItemData("Payment Methods", Icons.Default.Payment, "payment_methods"),
                        MenuItemData(
                            "Billing & Subscriptions",
                            Icons.Default.Receipt,
                            "billing_subscriptions"
                        ),
                        MenuItemData("Linked Accounts", Icons.Default.Link, "linked_accounts"),
                        MenuItemData("Data & Analytics", Icons.Default.Analytics, "data_analytics"),
                        MenuItemData("App Appearance", Icons.Default.Palette, "app_appearance"),
                        MenuItemData("Help & Support", Icons.Default.Help, "help_support"),
                        MenuItemData("Rate us", Icons.Default.Star, ""),
                    )

                    menuItems.forEach { item ->
                        MenuItemRow(
                            item = item,
                            onClick = {
                                if (item.route.isNotEmpty()) {
                                    navController.navigate(item.route)
                                }
                            }
                        )
                    }

                    // Logout
                    MenuItemRow(
                        item = MenuItemData("Logout", Icons.Default.ExitToApp, ""),
                        onClick = { /* Handle logout */ },
                        textColor = Color.Red
                    )
                }
            }
        }
    }
}
data class MenuItemData(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun MenuItemRow(
    item: MenuItemData,
    onClick: () -> Unit,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            item.icon,
            contentDescription = null,
            tint = textColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            item.title,
            modifier = Modifier.weight(1f),
            color = textColor,
            fontSize = 16.sp
        )
        if (item.route.isNotEmpty()) {
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}


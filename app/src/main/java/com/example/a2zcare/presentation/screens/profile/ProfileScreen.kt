package com.example.a2zcare.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.a2zcare.presentation.common_ui.MiniTopBar
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.navegation.bottomNavItems
import com.example.a2zcare.presentation.screens.home.BottomNavigationBar
import com.example.a2zcare.presentation.screens.vip_payment.VIPCard
import com.example.a2zcare.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    var selectedIndex by rememberSaveable { mutableIntStateOf(4) }
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsStateWithLifecycle()
    val showLogOutDialog by viewModel.showLogOutDialog.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }
    Scaffold(
        topBar = {
            MiniTopBar(
                title = "\uD83D\uDC64 Profile",
                navController = navController
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selectedIndex = selectedIndex,
                items = bottomNavItems,
                onItemSelected = { index ->
                    navController.navigate(bottomNavItems[index].route) {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        VIPCard(navController)
                    }
                    item {
                        ProfileHeader(
                            user = user,
                            onEditProfile = { navController.navigate(Screen.PersonalInformationScreen.route) }
                        )
                    }

                    item {
                        ProfileSection(
                            title = "Account",
                            items = listOf(
                                ProfileItem(
                                    icon = Icons.Default.Person,
                                    title = "Personal Information",
                                    subtitle = "Update your personal details",
                                    onClick = { navController.navigate(Screen.PersonalInformationScreen.route) }
                                ),
                                ProfileItem(
                                    icon = Icons.Default.Notifications,
                                    title = "Notifications",
                                    subtitle = "Manage your notification preferences",
                                    onClick = { navController.navigate(Screen.NotificationSittingScreen.route) }
                                ),
                                ProfileItem(
                                    icon = Icons.Default.Security,
                                    title = "Security",
                                    subtitle = "Password and security settings",
                                    onClick = { navController.navigate(Screen.SecuritySettingScreen.route) }
                                )
                            )
                        )
                    }

                    item {
                        ProfileSection(
                            title = "Billing",
                            items = listOf(
                                ProfileItem(
                                    icon = Icons.Default.CreditCard,
                                    title = "Payment Methods",
                                    subtitle = "Manage your payment methods",
                                    onClick = { navController.navigate(Screen.PaymentMethodsScreen.route) }
                                ),
                                ProfileItem(
                                    icon = Icons.Default.Subscriptions,
                                    title = "Subscriptions",
                                    subtitle = "View your active subscriptions",
                                    onClick = { navController.navigate(Screen.SubscriptionsScreen.route) }
                                )
                            )
                        )
                    }

                    item {
                        ProfileSection(
                            title = "Support",
                            items = listOf(
                                ProfileItem(
                                    icon = Icons.Default.Help,
                                    title = "Help & Support",
                                    subtitle = "Get help with your account",
                                    onClick = { navController.navigate(Screen.HelpSupportScreen.route) }
                                ),
                                ProfileItem(
                                    icon = Icons.Default.PrivacyTip,
                                    title = "Privacy Policy",
                                    subtitle = "Read our privacy policy",
                                    onClick = { navController.navigate(Screen.PrivacyPolicyScreen.route) }
                                ),
                                ProfileItem(
                                    icon = Icons.Default.Info,
                                    title = "About",
                                    subtitle = "App information and version",
                                    onClick = { navController.navigate(Screen.AboutScreen.route) }
                                )
                            )
                        )
                    }

                    item {
                        ProfileSection(
                            title = "Danger Zone",
                            items = listOf(
                                ProfileItem(
                                    icon = Icons.Default.DeleteForever,
                                    title = "Delete Account",
                                    subtitle = "Permanently delete your account",
                                    onClick = { viewModel.showDeleteDialog() },
                                    isDestructive = true
                                )
                            )
                        )
                    }

                    item {
                        Button(
                            onClick = { viewModel.showLogOutDialog() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Log Out")
                        }
                    }
                }
            }
        }

        error?.let { errorMessage ->
            LaunchedEffect(errorMessage) {
                // Show error snackbar or handle error
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.hideDeleteDialog() },
                title = { Text("Delete Account") },
                text = {
                    Text("Are you sure you want to delete your account? This action cannot be undone and all your data will be permanently removed.")
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.deleteAccount() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.hideDeleteDialog() }) {
                        Text("Cancel")
                    }
                }
            )
        }
        if (showLogOutDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.hideLogOutDialog() },
                title = { Text("LogOut") },
                text = {
                    Text("Are you sure you want to LogOut?")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.logout()
                            navController.navigate(Screen.LogIn.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("LogOut")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.hideLogOutDialog() }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileHeader(
    user: com.example.a2zcare.data.model.User,
    onEditProfile: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials(user.firstName, user.lastName, user.name, user.userName),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // User Name
            Text(
                text = getDisplayName(user.firstName, user.lastName, user.name, user.userName),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            // User Email
            Text(
                text = user.email,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Button
            OutlinedButton(
                onClick = onEditProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit Profile")
            }
        }
    }
}

@Composable
private fun ProfileSection(
    title: String,
    items: List<ProfileItem>
) {
    Column {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column {
                items.forEachIndexed { index, item ->
                    ProfileItemRow(
                        item = item,
                        showDivider = index < items.size - 1
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileItemRow(
    item: ProfileItem,
    showDivider: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { item.onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (item.isDestructive) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (item.isDestructive) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            Text(
                text = item.subtitle,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
        )
    }

    if (showDivider) {
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    }
}

private data class ProfileItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit,
    val isDestructive: Boolean = false
)

private fun getDisplayName(
    firstName: String?,
    lastName: String?,
    name: String?,
    userName: String?
): String {
    return when {
        !firstName.isNullOrBlank() && !lastName.isNullOrBlank() -> "$firstName $lastName"
        !name.isNullOrBlank() -> name
        !firstName.isNullOrBlank() -> firstName
        !userName.isNullOrBlank() -> userName
        else -> "User"
    }
}

private fun getInitials(
    firstName: String?,
    lastName: String?,
    name: String?,
    userName: String?
): String {
    return when {
        !firstName.isNullOrBlank() && !lastName.isNullOrBlank() ->
            "${firstName.first().uppercase()}${lastName.first().uppercase()}"

        !name.isNullOrBlank() -> {
            val parts = name.split(" ")
            if (parts.size >= 2) {
                "${parts[0].first().uppercase()}${parts[1].first().uppercase()}"
            } else {
                name.take(2).uppercase()
            }
        }

        !firstName.isNullOrBlank() -> firstName.take(2).uppercase()
        !userName.isNullOrBlank() -> userName.take(2).uppercase()
        else -> "U"
    }
}
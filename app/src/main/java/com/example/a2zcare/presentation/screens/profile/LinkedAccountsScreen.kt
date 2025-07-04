package com.example.a2zcare.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkedAccountsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Linked Accounts") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                "Connect your accounts to sync data and enhance your experience",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(16.dp)
            )

            val linkedAccounts = listOf(
                LinkedAccountData("Google", Icons.Default.AccountCircle, true, "Connected"),
                LinkedAccountData("Apple Health", Icons.Default.Favorite, true, "Connected"),
                LinkedAccountData("Facebook", Icons.Default.AccountCircle, false, "Not Connected"),
                LinkedAccountData("Twitter", Icons.Default.AccountCircle, false, "Not Connected"),
                LinkedAccountData("Instagram", Icons.Default.AccountCircle, false, "Not Connected"),
                LinkedAccountData("Fitbit", Icons.Default.FitnessCenter, false, "Not Connected"),
                LinkedAccountData("Samsung Health", Icons.Default.Favorite, false, "Not Connected")
            )

            linkedAccounts.forEach { account ->
                LinkedAccountRow(
                    account = account,
                    onToggle = { /* Handle toggle */ }
                )
            }
        }
    }
}

data class LinkedAccountData(
    val name: String,
    val icon: ImageVector,
    val isConnected: Boolean,
    val status: String
)

@Composable
fun LinkedAccountRow(
    account: LinkedAccountData,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                account.icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    account.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    account.status,
                    fontSize = 14.sp,
                    color = if (account.isConnected) Color.Green else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.7f
                    )
                )
            }

            Switch(
                checked = account.isConnected,
                onCheckedChange = onToggle
            )
        }
    }
}
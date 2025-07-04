package com.example.a2zcare.presentation.screens.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingSubscriptionsScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val subscriptions by viewModel.subscriptions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Billing & Subscriptions") },
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
            // Current Subscription
            subscriptions.forEach { subscription ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE91E63)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                subscription.name,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (subscription.discountPercentage > 0) {
                                Text(
                                    "${subscription.discountPercentage}% OFF",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier
                                        .background(
                                            Color.White.copy(alpha = 0.2f),
                                            RoundedCornerShape(12.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "$${subscription.price}",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "/${subscription.duration}",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 16.sp
                            )
                        }

                        if (subscription.discountPercentage > 0) {
                            val originalPrice = subscription.price / (1 - subscription.discountPercentage / 100.0)
                            Text(
                                "$${String.format("%.2f", originalPrice)}",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp,
                                textDecoration = TextDecoration.LineThrough
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        subscription.features.forEach { feature ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    feature,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { navController.navigate("choose_payment") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text(
                                "Continue with ${subscription.name}",
                                color = Color(0xFFE91E63),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Billing History
            Text(
                "Billing History",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            // Mock billing items
            val billingItems = listOf(
                BillingItem("A2Z Care Pro", "Dec 15, 2024", "$99.99", "Paid"),
                BillingItem("A2Z Care Pro", "Dec 15, 2023", "$99.99", "Paid"),
                BillingItem("A2Z Care Pro", "Dec 15, 2022", "$99.99", "Paid")
            )

            billingItems.forEach { item ->
                BillingItemRow(item = item)
            }
        }
    }
}

data class BillingItem(
    val name: String,
    val date: String,
    val amount: String,
    val status: String
)

@Composable
fun BillingItemRow(item: BillingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                item.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                item.date,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                item.amount,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                item.status,
                fontSize = 14.sp,
                color = Color.Green
            )
        }
    }
}

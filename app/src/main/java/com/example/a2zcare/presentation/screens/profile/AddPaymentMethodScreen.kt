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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentMethodScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Payment Method") },
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
                "Choose your payment method",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(16.dp)
            )

            // Payment Method Options
            val paymentMethods = listOf(
                PaymentMethodOption("PayPal", Icons.Default.AccountBalance, Color(0xFF003087)),
                PaymentMethodOption("Google Pay", Icons.Default.GppGood, Color(0xFF4285F4)),
                PaymentMethodOption("Apple Pay", Icons.Default.Api, Color(0xFF000000)),
                PaymentMethodOption("Mastercard", Icons.Default.CreditCard, Color(0xFFEB001B)),
                PaymentMethodOption("Visa", Icons.Default.CreditCard, Color(0xFF1A1F71)),
                PaymentMethodOption("American Express", Icons.Default.CreditCard, Color(0xFF006FCF))
            )

            paymentMethods.forEach { method ->
                PaymentMethodCard(
                    method = method,
                    onClick = {
                        // Add payment method logic
                        navController.navigate("choose_payment")
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Continue Button
            Button(
                onClick = { navController.navigate("choose_payment") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE91E63)
                )
            ) {
                Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

data class PaymentMethodOption(
    val name: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun PaymentMethodCard(
    method: PaymentMethodOption,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(method.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    method.icon,
                    contentDescription = null,
                    tint = method.color,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                method.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

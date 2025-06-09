package com.example.a2zcare.presentation.screens.get_start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.R
import com.example.a2zcare.presentation.common_ui.GoogleButton
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.selected


@Composable
fun GetStart(
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.app_main_icon),
                contentDescription = "Heart Icon",
                modifier = Modifier.size(72.dp)
            )

            Text(
                text = "Let's Get Started!",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White

            )

            Text(
                text = "Let's dive in into your account",
                style = MaterialTheme.typography.titleMedium,
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(16.dp))
            GoogleButton()
            Spacer(modifier = Modifier.height(32.dp))

            CustomButton(
                onClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                text = "Sign up",
                color = selected
            )
            CustomButton(
                onClick = {
                    navController.navigate(Screen.LogIn.route)
                },
                text = "Sign in",
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Privacy Policy",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "·",
                    color = Color.Gray
                )
                Text(
                    text = "Terms of Service",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewGetStart() {
    GetStart( navController = rememberNavController())
}

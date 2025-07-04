package com.example.a2zcare.presentation.screens.get_start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a2zcare.R
import com.example.a2zcare.presentation.common_ui.CustomButton
import com.example.a2zcare.presentation.common_ui.GoogleButton
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.selected
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun GetStart(
    navController: NavController
) {
    val signUpLoading = remember { mutableStateOf(false) }
    val signInLoading = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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
                    coroutineScope.launch {
                        signUpLoading.value = true
                        signUpLoading.value = false
                        navController.navigate(Screen.SignUp.route)
                    }
                },
                text = "Sign up",
                color = selected,
                loading = signUpLoading.value
            )

            CustomButton(
                onClick = {
                    coroutineScope.launch {
                        signInLoading.value = true
                        signInLoading.value = false
                        navController.navigate(Screen.LogIn.route)
                    }
                },
                text = "Sign in",
                color = Color.DarkGray,
                loading = signInLoading.value
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
                Text(text = "·", color = Color.Gray)
                Text(
                    text = "Terms of Service",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

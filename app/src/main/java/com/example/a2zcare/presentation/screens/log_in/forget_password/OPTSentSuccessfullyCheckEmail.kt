package com.example.a2zcare.presentation.screens.log_in.forget_password

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.a2zcare.presentation.common_ui.ConfirmationBottomBar
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor

@Composable
fun OPTSentSuccessfullyCheckEmail(
    navController: NavController
) {
    Scaffold(
        bottomBar = {
            ConfirmationBottomBar(
                buttonText = "Go to Login",
                enabled = true,
                onConfirmationClick = {
                    navController.navigate(Screen.LogIn.route)
                },
                content = null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.inbox),
                contentDescription = "check email"
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Check Your email to reset the Password",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

        }
    }


}

@Preview
@Composable
private fun PreviewOPTSentSuccessfullyCheckEmail() {
    OPTSentSuccessfullyCheckEmail(navController = rememberNavController())
}
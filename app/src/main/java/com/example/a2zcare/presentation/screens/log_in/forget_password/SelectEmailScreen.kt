package com.example.a2zcare.presentation.screens.log_in.forget_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.R
import com.example.a2zcare.presentation.common_ui.ConfirmationBottomBar
import com.example.a2zcare.presentation.common_ui.LargeTopBar
import com.example.a2zcare.presentation.common_ui.ValidatedTextField
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.viewmodel.forget_password_view_model.SelectEmailViewModel

@Composable
fun SelectEmailScreen(
    navController: NavController,
    viewModel: SelectEmailViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsState()
    val emailError by viewModel.emailError.collectAsState()

    Scaffold(
        topBar = {
            LargeTopBar(
                title = "Forget Password?\uD83D\uDD11",
                onBackActionClick = { navController.navigate(Screen.LogIn.route)}
            )
        },
        bottomBar = {
            ConfirmationBottomBar(
                buttonText = "Send OPT Code",
                enabled = email.isNotEmpty() && emailError == null,
                onConfirmationClick = { }
            )

        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ){
            Text(
                text = stringResource(R.string.forgot_password_description),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray,
                modifier = Modifier.padding(
                    start = 16.dp,end = 16.dp, top = 8.dp, bottom = 16.dp
                )
            )
            Text(
                text = "Registered email address",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp,end = 16.dp)
            )
            Spacer(Modifier.height(8.dp))

            ValidatedTextField(
                value = email,
                onValueChange = viewModel::onEmailChange,
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null, tint = Color.White)
                },
                placeholder = {
                    Text("Email", color = Color.Gray)
                },
                isError = emailError != null,
                errorMessage = emailError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp,end = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                )
            )
        }

    }

}

@Preview
@Composable
private fun PreviewSelectEmailScreen() {
    SelectEmailScreen( navController = rememberNavController())
}
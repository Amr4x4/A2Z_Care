package com.example.a2zcare.presentation.screens.log_in.forget_password

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.a2zcare.domain.model.NetworkResult
import com.example.a2zcare.presentation.common_ui.ConfirmationBottomBar
import com.example.a2zcare.presentation.common_ui.LargeTopBar
import com.example.a2zcare.presentation.common_ui.ValidatedTextField
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.successGreen
import com.example.a2zcare.presentation.viewmodel.forget_password_view_model.SelectEmailViewModel
import com.google.android.material.snackbar.Snackbar

@Composable
fun SelectEmailScreen(
    navController: NavController,
    viewModel: SelectEmailViewModel = hiltViewModel()
) {
    val email by viewModel.email.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val forgotPasswordResult by viewModel.forgotPasswordResult.collectAsState()

    val snackBarHostState = remember { SnackbarHostState() }
    val snackBarColor = remember { mutableStateOf(successGreen) }


    LaunchedEffect(forgotPasswordResult) {
        forgotPasswordResult?.let { result ->
            when (result) {
                is NetworkResult.Success -> {
                    val message = result.data?.message?.takeIf { it.isNotBlank() }
                        ?: "Check Your Email."
                    snackBarColor.value = successGreen
                    snackBarHostState.showSnackbar(message)
                    viewModel.clearResult()
                    navController.navigate(Screen.OPTSentSuccessfullyCheckEmail.route)
                }
                is NetworkResult.Error -> {
                    val errorMessage = result.message?.takeIf { it.isNotBlank() }
                        ?: "An unexpected error occurred. Please try again."
                    snackBarColor.value = Color.Red
                    snackBarHostState.showSnackbar(errorMessage)
                    viewModel.clearResult()
                }
                is NetworkResult.Loading -> {
                    // Optional: Show global loading indicator
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { snackBarData: SnackbarData ->
                    Snackbar(
                        snackbarData = snackBarData,
                        containerColor = snackBarColor.value,
                        contentColor = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        },
        topBar = {
            LargeTopBar(
                title = "Forget Password? 🔑",
                onBackActionClick = { navController.navigate(Screen.LogIn.route) }
            )
        },
        bottomBar = {
            ConfirmationBottomBar(
                buttonText = if (isLoading) "" else "Rest Password",
                enabled = email.isNotEmpty() && emailError == null && !isLoading,
                onConfirmationClick = {
                    viewModel.sendForgetPasswordRequest()
                },
                content = if (isLoading) {
                    {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White
                            )
                        }
                    }
                } else null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.forgot_password_description),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray,
                modifier = Modifier.padding(
                    start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp
                )
            )

            Text(
                text = "Registered email address",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
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
                    .padding(start = 16.dp, end = 16.dp),
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
    SelectEmailScreen(navController = rememberNavController())
}

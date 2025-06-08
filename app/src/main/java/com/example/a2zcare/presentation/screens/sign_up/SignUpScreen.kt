package com.example.a2zcare.presentation.screens.sign_up

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.presentation.common_ui.ConfirmButton
import com.example.a2zcare.presentation.common_ui.ValidatedTextField
import com.example.a2zcare.presentation.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val confirmPassword by viewModel.confirmPassword.collectAsState()
    val passwordVisible by viewModel.passwordVisible.collectAsState()
    val confirmPasswordVisible by viewModel.confirmPasswordVisible.collectAsState()
    val agreedToTerms by viewModel.agreedToTerms.collectAsState()
    val emailError by viewModel.emailError.collectAsState()
    val passwordError by viewModel.passwordError.collectAsState()
    val confirmPasswordError by viewModel.confirmPasswordError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSignUpEnabled by viewModel.isSignUpEnabled.collectAsState()
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.signUpResult.collectLatest { result ->
            result.onSuccess {
                Toast.makeText(context, "Sign Up Successful!", Toast.LENGTH_LONG).show()
                // Navigate to login or home screen
                // navController.navigate("login")
            }
            result.onFailure { e ->
                snackbarHostState.showSnackbar("Sign Up Failed: ${e.message ?: "Unknown error"}")
            }
        }
    }

    Scaffold(
        topBar = {
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ValidatedTextField(
                value = userName,
                onValueChange = viewModel::onUserNameChange,
                label = "Username",
                isError = false,
                errorMessage = null,
                modifier = Modifier.padding(bottom = 12.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )
            ValidatedTextField(
                value = email,
                onValueChange = viewModel::onEmailChange,
                label = "Email",
                isError = emailError != null,
                errorMessage = emailError,
                modifier = Modifier.padding(bottom = 12.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                )
            )
            ValidatedTextField(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = "Create Password",
                isError = passwordError != null,
                errorMessage = passwordError,
                modifier = Modifier.padding(bottom = 12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                )
            )
            ValidatedTextField(
                value = confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = "Confirm Password",
                isError = confirmPasswordError != null,
                errorMessage = confirmPasswordError,
                modifier = Modifier.padding(bottom = 12.dp),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = viewModel::onToggleConfirmPasswordVisibility) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = agreedToTerms,
                    onCheckedChange = viewModel::onAgreeToTermsChanged
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("I agree to the Terms and Conditions", style = MaterialTheme.typography.bodyMedium)
            }

            ConfirmButton(
                enabled = isSignUpEnabled,
                onClick = viewModel::signUp,
                isLoading = isLoading,
                modifier = Modifier.padding(bottom = 12.dp)
            )

        }
    }
}

@Preview
@Composable
private fun PreviewSignUp() {
    SignUpScreen( navController = rememberNavController() )
}
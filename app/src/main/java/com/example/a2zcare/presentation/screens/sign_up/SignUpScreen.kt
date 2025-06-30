package com.example.a2zcare.presentation.screens.sign_up

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.R
import com.example.a2zcare.presentation.common_ui.ConfirmButton
import com.example.a2zcare.presentation.common_ui.GoogleButton
import com.example.a2zcare.presentation.common_ui.SigningTopAppBar
import com.example.a2zcare.presentation.common_ui.ValidatedTextField
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.viewmodel.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

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

    val snackBarHostState = remember { SnackbarHostState() }

    var isTermsAndConditionsDialogOpen by rememberSaveable { mutableStateOf(false) }

    TermsAndConditionsAlertDialog(
        isOpen = isTermsAndConditionsDialogOpen,
        title = "Terms & Conditions",
        bodyText = stringResource(R.string.terms_conditions_full),
        onDismissRequest = { isTermsAndConditionsDialogOpen = false },
        onConfirmButtonClick = {
            viewModel.onAgreeToTermsChanged(true)
            isTermsAndConditionsDialogOpen = false
        }
    )
    LaunchedEffect(Unit) {
        viewModel.signUpResult.collectLatest { result ->
            result.onSuccess { signUpResponse ->
                Log.d("SignUpScreen", "SignUp Success: $signUpResponse")
                Toast.makeText(
                    context,
                    "Sign Up Successful! Welcome ${signUpResponse.userName ?: "User"}!",
                    Toast.LENGTH_LONG
                ).show()

                navController.navigate(Screen.PersonalOnBoarding.route) {
                    popUpTo(Screen.SignUp.route) { inclusive = true }
                }
            }
            result.onFailure { e ->
                Log.e("SignUpScreen", "SignUp Failure: ${e.message}")
                snackBarHostState.showSnackbar("Sign Up Failed: ${e.message ?: "Unknown error"}")
            }
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SigningTopAppBar(
                title = "Join A2Z Care Today \uD83E\uDE7A",
                onBackButtonClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.GetStart.route)
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "Create your account and start monitoring your health",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    color = Color.White
                )
                Spacer(Modifier.padding(vertical = 12.dp))
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(backgroundColor)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "UserName",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))
                    ValidatedTextField(
                        value = userName,
                        onValueChange = viewModel::onUserNameChange,
                        leadingIcon = {
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = Color.White
                            )
                        },
                        placeholder = {
                            Text("UserName", color = Color.Gray)
                        },
                        isError = false,
                        errorMessage = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        ),
                    )

                    Spacer(Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
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
                            .padding(bottom = 12.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email
                        )
                    )
                    Spacer(Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Create Password",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))

                    ValidatedTextField(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                        },
                        placeholder = {
                            Text("Create Password", color = Color.Gray)
                        },
                        isError = passwordError != null,
                        errorMessage = passwordError,
                        modifier = Modifier.padding(bottom = 12.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image =
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                                Icon(imageVector = image, contentDescription = null, tint = Color.White)
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        )
                    )

                    Spacer(Modifier.padding(vertical = 8.dp))

                    Text(
                        text = "Confirm Password",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))

                    ValidatedTextField(
                        value = confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                        },
                        placeholder = {
                            Text("Confirm Password", color = Color.Gray)
                        },
                        isError = confirmPasswordError != null,
                        errorMessage = confirmPasswordError,
                        modifier = Modifier.padding(bottom = 12.dp),
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = viewModel::onToggleConfirmPasswordVisibility) {
                                Icon(imageVector = image, contentDescription = null, tint = Color.White)
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreedToTerms,
                            onCheckedChange = viewModel::onAgreeToTermsChanged,
                            colors = CheckboxDefaults.colors(
                                checkmarkColor = Color.White,
                                uncheckedColor = Color.White,
                                checkedColor = Color.Red
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "I agree to the ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Text(
                            text = "Terms and Conditions",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Red,
                            modifier = Modifier.clickable {
                                isTermsAndConditionsDialogOpen = true
                            }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Already have an account? ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Text(
                            text = "Sign In",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.Red,
                            modifier = Modifier.clickable {
                                navController.navigate(Screen.LogIn.route) {
                                    popUpTo(Screen.SignUp.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    Spacer(Modifier.padding(vertical = 12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color.Gray
                        )
                        Text(
                            text = " or ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color.Gray
                        )
                    }

                    Spacer(Modifier.padding(vertical = 12.dp))
                    GoogleButton()
                    Spacer(Modifier.padding(vertical = 12.dp))

                    ConfirmButton(
                        text = "Sign Up",
                        onClick = viewModel::signUp,
                        enabled = isSignUpEnabled,
                        isLoading = isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}
package com.example.a2zcare.presentation.screens.log_in

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.a2zcare.presentation.common_ui.ConfirmButton
import com.example.a2zcare.presentation.common_ui.GoogleButton
import com.example.a2zcare.presentation.common_ui.SigningTopAppBar
import com.example.a2zcare.presentation.common_ui.ValidatedTextField
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val isLoginEnabled by viewModel.isLoginEnabled.collectAsStateWithLifecycle()
    val emailError by viewModel.emailError.collectAsStateWithLifecycle()
    val passwordError by viewModel.passwordError.collectAsStateWithLifecycle()
    val passwordVisible by viewModel.passwordVisible.collectAsStateWithLifecycle()
    var isChecked by remember { mutableStateOf(false) }


    val snackBarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            Toast.makeText(context, "Log In Successful!", Toast.LENGTH_LONG).show()
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.LogIn.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        modifier =
            Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SigningTopAppBar(
                title = "Welcome Back! \uD83D\uDC4B",
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
                    text = "Log in to Continue your health journey",
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
                    Spacer(Modifier.padding(vertical = 10.dp))

                    Text(
                        text = "Password",
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
                            Text("Password", color = Color.Gray)
                        },
                        isError = passwordError != null,
                        errorMessage = passwordError,
                        modifier = Modifier.padding(bottom = 12.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image =
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                                Icon(imageVector = image, contentDescription = null)
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Password
                        )
                    )
                    Spacer(Modifier.padding(vertical = 10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isChecked,
                                onCheckedChange = { isChecked = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color.Red)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Remember me",
                                color = Color.White
                            )
                        }

                        Text(
                            modifier = Modifier
                                .clickable(
                                    onClick = {
                                        navController.navigate(Screen.SelectEmail.route)
                                    }
                                ),
                            text = "Forget Password?",
                            textDecoration = TextDecoration.Underline,
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Don't have an account? ", color = Color.White)
                        Text(
                            modifier = Modifier.clickable
                                (
                                onClick = {
                                    navController.navigate(Screen.SignUp.route)
                                }
                            ),
                            text = "Sign Up",
                            color = Color.Red,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Bold
                        )

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color.DarkGray
                        )
                        Text(" or continue with ", color = Color.Gray, fontSize = 12.sp)
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color.DarkGray
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    GoogleButton()
                    Spacer(modifier = Modifier.height(32.dp))

                    ConfirmButton(
                        enabled = isLoginEnabled,
                        onClick = {
                            viewModel.login(
                                email = email,
                                password = password,
                            )
                        },
                        isLoading = isLoading,
                        text = "Log In",
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}

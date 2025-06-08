package com.example.a2zcare.presentation.screens.log_in

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.a2zcare.presentation.screens.sign_up.TermsAndConditionsAlertDialog
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
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.R
import com.example.a2zcare.presentation.common_ui.SigningTopAppBar
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.fieldColor
import com.example.a2zcare.presentation.viewmodel.SignUpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    navController: NavController,
    viewModel: SignUpViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val passwordVisible by viewModel.passwordVisible.collectAsState()
    val agreedToTerms by viewModel.agreedToTerms.collectAsState()

    var isTermsAndConditionsDialogOpen by rememberSaveable { mutableStateOf( false ) }

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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SigningTopAppBar(
                title = "Join A2Z Care Today \uD83E\uDE7A",
                onBackButtonClick = {
                    navController.popBackStack()
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValue)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    text = "Create your account and start monitoring your health",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    color = Color.White
                )
            }
            item {
                Spacer(Modifier.padding(vertical = 18.dp))
            }
            item {
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
                    OutlinedTextField(
                        value = email,
                        onValueChange = { viewModel::onEmailChange },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color.White)
                        },
                        placeholder = {
                            Text("Email", color = Color.Gray)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = fieldColor,
                            focusedContainerColor = fieldColor,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedLeadingIconColor = Color.White,
                            focusedLeadingIconColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.padding(vertical = 16.dp))

                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { viewModel::onPasswordChange },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White)
                        },
                        placeholder = {
                            Text("Password", color = Color.Gray)
                        },
                        trailingIcon = {
                            val visibilityIcon =
                                if (passwordVisible) {
                                    painterResource(R.drawable.close_eye)
                                } else {
                                    painterResource(R.drawable.open_eye)
                                }
                            IconButton(onClick = { viewModel.onTogglePasswordVisibility() }) {
                                Icon(visibilityIcon, contentDescription = null, tint = Color.White)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = fieldColor,
                            focusedContainerColor = fieldColor,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedLeadingIconColor = Color.White,
                            focusedLeadingIconColor = Color.White,
                            unfocusedTrailingIconColor = Color.White,
                            focusedTrailingIconColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreedToTerms,
                            onCheckedChange = viewModel::onAgreeToTermsChanged,
                            colors = CheckboxDefaults.colors(checkedColor = Color.Red)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("I agree to A2ZCare ", color = Color.White)
                        Text(
                            modifier = Modifier.clickable(onClick = { isTermsAndConditionsDialogOpen = true }),
                            text = "Terms & Conditions",
                            color = Color.Red
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Already have an account? ", color = Color.White)
                        Text(
                            modifier = Modifier.clickable(onClick = { navController.navigate(Screen.LogIn.route) }),
                            text = "Sign in",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Divider(modifier = Modifier.weight(1f), color = Color.DarkGray)
                        Text(" or continue with ", color = Color.Gray, fontSize = 12.sp)
                        Divider(modifier = Modifier.weight(1f), color = Color.DarkGray)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }

}

@Preview
@Composable
private fun PreviewSignUpScreen() {
    LogInScreen(navController = rememberNavController())
}
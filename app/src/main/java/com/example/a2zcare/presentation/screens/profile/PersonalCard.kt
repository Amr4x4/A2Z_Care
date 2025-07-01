package com.example.a2zcare.presentation.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.R
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.viewmodel.UserViewModel
import android.util.Log

@Composable
fun PersonalCard(
    username: String,
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    /*
    val uiState by viewModel.uiState.collectAsState()

    // Debug logging
    LaunchedEffect(username) {
        Log.d("PersonalCard", "LaunchedEffect triggered with username: '$username'")
        if (username.isNotEmpty()) {
            viewModel.fetchUser(username)
        } else {
            Log.w("PersonalCard", "Username is empty!")
        }
    }

    // Log state changes
    LaunchedEffect(uiState) {
        Log.d("PersonalCard", "UI State changed - Loading: ${uiState.isLoading}, User: ${uiState.user?.userName}, Error: ${uiState.error}")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(10.dp)
            .clickable {
                navController.navigate(Screen.PersonalInformationScreen.route)
            },
        colors = CardDefaults.cardColors(
            containerColor = fieldCardColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.avatar_dark_null),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .padding(10.dp)
                    .background(
                        shape = CircleShape,
                        color = Color.White
                    ),
                alignment = Alignment.Center,
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                when {
                    uiState.isLoading -> {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Loading user data...",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    uiState.error != null -> {
                        Text(
                            text = username.ifEmpty { "Unknown User" },
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Error: ${uiState.error}",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Log.e("PersonalCard", "Error state: ${uiState.error}")
                    }

                    uiState.user != null -> {
                        Log.d("PersonalCard", "Displaying user data: ${uiState.user!!.userName}")
                        Text(
                            text = uiState.user!!.userName,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = uiState.user!!.email,
                            color = Color.DarkGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "Role: ${uiState.user!!.role}",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    else -> {
                        Log.d("PersonalCard", "Default state - showing username: '$username'")
                        Text(
                            text = username.ifEmpty { "Guest User" },
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Tap to load details",
                            color = Color.DarkGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            IconButton(
                onClick = {
                    navController.navigate(Screen.PersonalInformationScreen.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Personal Information",
                    tint = Color.Gray
                )
            }
        }
    }
     */
}

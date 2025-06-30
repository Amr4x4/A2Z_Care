package com.example.a2zcare.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.a2zcare.R
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.darkRed
import com.example.a2zcare.presentation.theme.lightRed
import com.example.a2zcare.presentation.viewmodel.UserViewModel
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    username: String,
    onBackButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: UserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Debug logging
    LaunchedEffect(username) {
        Log.d("HomeTopBar", "LaunchedEffect triggered with username: '$username'")
        if (username.isNotEmpty()) {
            viewModel.fetchUser(username)
        } else {
            Log.w("HomeTopBar", "Username is empty!")
        }
    }

    // Log state changes
    LaunchedEffect(uiState) {
        Log.d("HomeTopBar", "UI State changed - Loading: ${uiState.isLoading}, User: ${uiState.user?.userName}, Error: ${uiState.error}")
    }

    LargeTopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = backgroundColor,
            scrolledContainerColor = backgroundColor
        ),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(R.drawable.app_main_icon),
                    contentDescription = "App Main Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(35.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.DarkGray
                )
                IconButton(
                    modifier = Modifier
                        .size(width = 100.dp, height = 30.dp)
                        .background(
                            brush = Brush.verticalGradient(listOf(lightRed, darkRed)),
                            shape = RoundedCornerShape(20.dp)
                        ),
                    onClick = onBackButtonClick
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.pro_icon),
                            contentDescription = "Become Pro member now",
                            modifier = Modifier.width(20.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Get Pro",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        },
        title = {
            val welcomeText = when {
                uiState.isLoading -> "Loading..."
                uiState.user != null -> "Welcome ${uiState.user!!.userName} 👋"
                uiState.error != null -> "Welcome $username 👋 (Error)"
                username.isNotEmpty() -> "Welcome $username 👋"
                else -> "Welcome 👋"
            }

            Log.d("HomeTopBar", "Displaying welcome text: '$welcomeText'")

            if (uiState.isLoading) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = welcomeText,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                }
            } else {
                Text(
                    text = welcomeText,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    )
}
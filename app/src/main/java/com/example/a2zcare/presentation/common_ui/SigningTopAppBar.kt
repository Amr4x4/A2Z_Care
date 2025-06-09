package com.example.a2zcare.presentation.common_ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import com.example.a2zcare.presentation.theme.backgroundColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SigningTopAppBar(
    title: String,
    onBackButtonClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    LargeTopAppBar(
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = backgroundColor,
            scrolledContainerColor = backgroundColor
        ),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(
                onClick = { onBackButtonClick },
            ) {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Navigation Back",
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}
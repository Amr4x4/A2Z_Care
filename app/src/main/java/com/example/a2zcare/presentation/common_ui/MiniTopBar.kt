package com.example.a2zcare.presentation.common_ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.presentation.theme.fieldColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiniTopBar(
    title:String,
    navController: NavController
) {
    TopAppBar (
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {navController.popBackStack()}
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Icon Button",
                    tint = Color.LightGray
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = fieldColor,
            scrolledContainerColor = fieldColor
        )
    )

}

@Preview
@Composable
private fun PreviewMiniTopBar() {
    MiniTopBar(
        title = "MyApp",
        navController = rememberNavController()
    )
}
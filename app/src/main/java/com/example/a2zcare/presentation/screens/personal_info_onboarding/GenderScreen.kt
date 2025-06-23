package com.example.a2zcare.presentation.screens.personal_info_onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun GenderScreen(navController: NavController) {
    var selectedGender by remember { mutableStateOf("Male") }

    ScreenContent(title = "What's your gender?", progress = 0.32f) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            GenderOption("Male", selectedGender == "Male") { selectedGender = "Male" }
            GenderOption("Female", selectedGender == "Female") { selectedGender = "Female" }
        }
        Spacer(modifier = Modifier.height(16.dp))
        ContinueButton { navController.navigate("birthday") }
    }
}

@Composable
fun GenderOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(if (selected) Color.Red else Color.Gray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text.first().toString(), color = Color.White, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text, color = if (selected) Color.Red else Color.White)
    }
}

@Preview
@Composable
private fun PreviewGenderScreen() {
    GenderScreen(navController = rememberNavController())
}
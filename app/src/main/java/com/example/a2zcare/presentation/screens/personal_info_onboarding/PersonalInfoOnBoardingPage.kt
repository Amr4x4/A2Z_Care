package com.example.a2zcare.presentation.screens.personal_info_onboarding
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun PersonalInfoOnBoarding(
    navController: NavController
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "name") {
        composable("name") { NameScreen(navController) }
        composable("gender") { GenderScreen(navController) }
        composable("birthday") { BirthdayScreen(navController) }
        composable("weight") { WeightScreen(navController) }
        composable("height") { HeightScreen(navController) }
        composable("reminders") { RemindersScreen(navController) }
    }
}

@Composable
fun NameScreen(navController: NavController) {
    var name by remember { mutableStateOf(TextFieldValue("")) }

    ScreenContent(title = "What's your name?", progress = 0.16f) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Name") },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        ContinueButton(enabled = name.text.isNotBlank()) {
            navController.navigate("gender")
        }
    }
}


@Composable
fun BirthdayScreen(navController: NavController) {
    var month by remember { mutableStateOf(1) }
    var day by remember { mutableStateOf(1) }
    var year by remember { mutableStateOf(2000) }

    ScreenContent(title = "When is your birthday?", progress = 0.48f) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberPicker(value = month, range = 1..12, onValueChange = { month = it })
            NumberPicker(value = day, range = 1..31, onValueChange = { day = it })
            NumberPicker(value = year, range = 1950..2025, onValueChange = { year = it })
        }
        Spacer(modifier = Modifier.height(16.dp))
        ContinueButton { navController.navigate("weight") }
    }
}

@Composable
fun WeightScreen(navController: NavController) {
    var weight by remember { mutableStateOf(75) }

    ScreenContent(title = "What's your body weight?", progress = 0.64f) {
        NumberPicker(value = weight, range = 30..200, onValueChange = { weight = it })
        Spacer(modifier = Modifier.height(16.dp))
        ContinueButton { navController.navigate("height") }
    }
}

@Composable
fun HeightScreen(navController: NavController) {
    var height by remember { mutableStateOf(170) }

    ScreenContent(title = "How tall are you?", progress = 0.80f) {
        NumberPicker(value = height, range = 100..250, onValueChange = { height = it })
        Spacer(modifier = Modifier.height(16.dp))
        ContinueButton { navController.navigate("reminders") }
    }
}

@Composable
fun RemindersScreen(navController: NavController) {
    var hour by remember { mutableStateOf(9) }
    var minute by remember { mutableStateOf(0) }

    ScreenContent(title = "Health check reminders?", progress = 0.96f) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            NumberPicker(value = hour, range = 0..23, onValueChange = { hour = it })
            NumberPicker(value = minute, range = 0..59, onValueChange = { minute = it })
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate("loading") },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finish", color = Color.White)
        }
    }
}

@Composable
fun LoadingScreen(navController :NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color.Red)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Creating your account...", color = Color.White)
        }
    }
}

@Composable
fun ScreenContent(title: String, progress: Float, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LinearProgressIndicator(progress = progress, color = Color.Red, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(32.dp))
        Text(title, color = Color.White, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(32.dp))
        content()
    }
}



@Composable
fun NumberPicker(value: Int, range: IntRange, onValueChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("▲", color = Color.Red, modifier = Modifier.clickable { onValueChange((value + 1).coerceAtMost(range.last)) })
        Spacer(modifier = Modifier.height(4.dp))
        Text(value.toString(), color = Color.White, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text("▼", color = Color.Red, modifier = Modifier.clickable { onValueChange((value - 1).coerceAtLeast(range.first)) })
    }
}

@Composable
fun ContinueButton(enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        shape = RoundedCornerShape(50),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Continue", color = Color.White)
    }
}

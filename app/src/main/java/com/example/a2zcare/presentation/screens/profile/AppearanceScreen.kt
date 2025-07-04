package com.example.a2zcare.presentation.screens.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.domain.model.AppTheme
import com.example.a2zcare.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userPreferences by viewModel.userPreferences.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Appearance") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Theme Section
            Text(
                "Theme",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            val themes = listOf(
                AppTheme.LIGHT to "Light",
                AppTheme.DARK to "Dark",
                AppTheme.SYSTEM_DEFAULT to "System Default"
            )

            themes.forEach { (theme, themeName) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = userPreferences.theme == theme,
                            onClick = {
                                viewModel.updateUserPreferences(
                                    userPreferences.copy(theme = theme)
                                )
                            }
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = userPreferences.theme == theme,
                        onClick = {
                            viewModel.updateUserPreferences(
                                userPreferences.copy(theme = theme)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        themeName,
                        fontSize = 16.sp
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            // Language Section
            Text(
                "Language",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            val languages = listOf(
                "English",
                "Spanish",
                "French",
                "German",
                "Italian",
                "Portuguese",
                "Russian",
                "Chinese",
                "Japanese",
                "Korean"
            )

            languages.forEach { language ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = userPreferences.language == language,
                            onClick = {
                                viewModel.updateUserPreferences(
                                    userPreferences.copy(language = language)
                                )
                            }
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = userPreferences.language == language,
                        onClick = {
                            viewModel.updateUserPreferences(
                                userPreferences.copy(language = language)
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        language,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
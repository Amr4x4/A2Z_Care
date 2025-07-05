package com.example.a2zcare.presentation.screens.profile

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.a2zcare.presentation.common_ui.MiniTopBar
import com.example.a2zcare.presentation.theme.backgroundColor
import com.example.a2zcare.presentation.theme.fieldCardColor
import com.example.a2zcare.presentation.viewmodel.ProfileViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.error.collectAsStateWithLifecycle()
    val updateSuccess by viewModel.updateSuccess.collectAsStateWithLifecycle()

    val context = LocalContext.current

    // Form states
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var healthGoals by remember { mutableStateOf("") }

    // Dropdown states
    var genderExpanded by remember { mutableStateOf(false) }
    var healthGoalsExpanded by remember { mutableStateOf(false) }

    // Date picker
    var showDatePicker by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()

    val genderOptions = listOf("Male", "Female", "Other")
    val healthGoalsOptions = listOf(
        "Weight Loss",
        "Weight Gain",
        "Muscle Building",
        "General Health",
        "Disease Management"
    )

    // Load user data when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    // Initialize form with user data
    LaunchedEffect(user) {
        if (user.id.isNotEmpty()) {
            // Add validation to check if firstName contains ID
            val userFirstName = user.firstName ?: ""
            val userId = user.id

            // Debug logging
            Log.d("PersonalInfoScreen", "User ID: $userId")
            Log.d("PersonalInfoScreen", "User firstName: $userFirstName")

            // Check if firstName is actually the ID (common issue)
            if (userFirstName == userId) {
                Log.w("PersonalInfoScreen", "WARNING: firstName contains user ID!")
                firstName = "" // Clear it instead of using the ID
            } else {
                firstName = userFirstName
            }

            lastName = user.lastName ?: ""
            phoneNumber = user.phoneNumber ?: ""
            address = user.address ?: ""
            dateOfBirth = user.dateOfBirth ?: ""
            gender = user.gender ?: ""
            age = user.age?.toString() ?: ""
            weight = user.weightKg?.toString() ?: ""
            height = user.height?.toString() ?: ""
            healthGoals = user.healthGoals ?: ""
        }
    }

    // Handle update success
    LaunchedEffect(updateSuccess) {
        if (!updateSuccess.isNullOrEmpty()) {
            Toast.makeText(context, updateSuccess, Toast.LENGTH_SHORT).show()
            viewModel.clearSuccess()
            navController.popBackStack()
        }
    }

    // Handle errors
    LaunchedEffect(errorMessage) {
        errorMessage?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // Date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                dateOfBirth = dateFormat.format(calendar.time)

                // Calculate age
                val today = Calendar.getInstance()
                val birthDate = Calendar.getInstance()
                birthDate.set(year, month, dayOfMonth)
                var calculatedAge = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
                if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                    calculatedAge--
                }
                age = calculatedAge.toString()

                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            MiniTopBar(
                title = "Edit Profile",
                navController = navController
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(backgroundColor)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        // Personal Details Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = fieldCardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Personal Details",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1976D2),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                // First Name
                                OutlinedTextField(
                                    value = firstName,
                                    onValueChange = { firstName = it },
                                    label = { Text("First Name") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF1976D2),
                                        focusedLabelColor = Color(0xFF1976D2)
                                    )
                                )

                                // Last Name
                                OutlinedTextField(
                                    value = lastName,
                                    onValueChange = { lastName = it },
                                    label = { Text("Last Name") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF1976D2),
                                        focusedLabelColor = Color(0xFF1976D2)
                                    )
                                )

                                // Phone Number
                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    label = { Text("Phone Number") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF1976D2),
                                        focusedLabelColor = Color(0xFF1976D2)
                                    )
                                )

                                // Address
                                OutlinedTextField(
                                    value = address,
                                    onValueChange = { address = it },
                                    label = { Text("Address") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    maxLines = 2,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF1976D2),
                                        focusedLabelColor = Color(0xFF1976D2)
                                    )
                                )

                                // Date of Birth
                                OutlinedTextField(
                                    value = if (dateOfBirth.isNotEmpty()) {
                                        try {
                                            val inputFormat = SimpleDateFormat(
                                                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                                                Locale.getDefault()
                                            )
                                            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                            val date = inputFormat.parse(dateOfBirth)
                                            date?.let { outputFormat.format(it) } ?: ""
                                        } catch (e: Exception) {
                                            ""
                                        }
                                    } else "",
                                    onValueChange = { },
                                    label = { Text("Date of Birth") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp)
                                        .clickable { showDatePicker = true },
                                    enabled = false,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        disabledBorderColor = Color(0xFF1976D2),
                                        disabledLabelColor = Color(0xFF1976D2)
                                    ),
                                    trailingIcon = {
                                        IconButton(onClick = { showDatePicker = true }) {
                                            Icon(
                                                imageVector = Icons.Default.CalendarToday,
                                                contentDescription = "Select Date",
                                                tint = Color(0xFF1976D2)
                                            )
                                        }
                                    }
                                )

                                // Gender Dropdown
                                ExposedDropdownMenuBox(
                                    expanded = genderExpanded,
                                    onExpandedChange = { genderExpanded = !genderExpanded }
                                ) {
                                    OutlinedTextField(
                                        value = gender,
                                        onValueChange = { },
                                        label = { Text("Gender") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp)
                                            .menuAnchor(),
                                        readOnly = true,
                                        trailingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Dropdown"
                                            )
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF1976D2),
                                            focusedLabelColor = Color(0xFF1976D2)
                                        )
                                    )

                                    ExposedDropdownMenu(
                                        expanded = genderExpanded,
                                        onDismissRequest = { genderExpanded = false }
                                    ) {
                                        genderOptions.forEach { option ->
                                            DropdownMenuItem(
                                                text = { Text(option) },
                                                onClick = {
                                                    gender = option
                                                    genderExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Health Information Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = fieldCardColor),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Health Information",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF1976D2),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                // Age
                                OutlinedTextField(
                                    value = age,
                                    onValueChange = {
                                        if (it.all { char -> char.isDigit() }) age = it
                                    },
                                    label = { Text("Age") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF1976D2),
                                        focusedLabelColor = Color(0xFF1976D2)
                                    )
                                )

                                // Weight
                                OutlinedTextField(
                                    value = weight,
                                    onValueChange = { weight = it },
                                    label = { Text("Weight (kg)") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF1976D2),
                                        focusedLabelColor = Color(0xFF1976D2)
                                    )
                                )

                                // Height
                                OutlinedTextField(
                                    value = height,
                                    onValueChange = {
                                        if (it.all { char -> char.isDigit() }) height = it
                                    },
                                    label = { Text("Height (cm)") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 12.dp),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color(0xFF1976D2),
                                        focusedLabelColor = Color(0xFF1976D2)
                                    )
                                )

                                // Health Goals Dropdown
                                ExposedDropdownMenuBox(
                                    expanded = healthGoalsExpanded,
                                    onExpandedChange = { healthGoalsExpanded = !healthGoalsExpanded }
                                ) {
                                    OutlinedTextField(
                                        value = healthGoals,
                                        onValueChange = { },
                                        label = { Text("Health Goals") },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = 12.dp)
                                            .menuAnchor(),
                                        readOnly = true,
                                        trailingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowDown,
                                                contentDescription = "Dropdown"
                                            )
                                        },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = Color(0xFF1976D2),
                                            focusedLabelColor = Color(0xFF1976D2)
                                        )
                                    )

                                    ExposedDropdownMenu(
                                        expanded = healthGoalsExpanded,
                                        onDismissRequest = { healthGoalsExpanded = false }
                                    ) {
                                        healthGoalsOptions.forEach { option ->
                                            DropdownMenuItem(
                                                text = { Text(option) },
                                                onClick = {
                                                    healthGoals = option
                                                    healthGoalsExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Update Button
                        Button(
                            onClick = {
                                // FIXED: Call updateUser with proper parameters
                                viewModel.updateUser(
                                    firstName = firstName.ifEmpty { null },
                                    lastName = lastName.ifEmpty { null },
                                    phoneNumber = phoneNumber.ifEmpty { null },
                                    address = address.ifEmpty { null },
                                    age = age.toIntOrNull(),
                                    dateOfBirth = dateOfBirth.ifEmpty { null },
                                    gender = gender.ifEmpty { null },
                                    weightKg = weight.toDoubleOrNull(),
                                    height = height.toIntOrNull(),
                                    healthGoals = healthGoals.ifEmpty { null }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2)
                            ),
                            enabled = !isLoading
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            } else {
                                Text(
                                    text = "Update Profile",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
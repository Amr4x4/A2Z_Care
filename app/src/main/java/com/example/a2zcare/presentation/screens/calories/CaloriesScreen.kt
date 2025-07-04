// Fixed version of package com.example.a2zcare.presentation.screens.calories

package com.example.a2zcare.presentation.screens.calories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.a2zcare.domain.entities.CaloriesIntakeEntry
import com.example.a2zcare.domain.entities.FoodItem
import com.example.a2zcare.presentation.navegation.Screen
import com.example.a2zcare.presentation.viewmodel.CalorieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaloriesScreen(
    modifier: Modifier = Modifier,
    viewModel: CalorieViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddFoodDialog by remember { mutableStateOf(false) }
    var showCustomFoodDialog by remember { mutableStateOf(false) }
    var showGoalDialog by remember { mutableStateOf(false) }
    var selectedMealType by remember { mutableStateOf("Breakfast") }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Calories Tracking") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showGoalDialog = true }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddFoodDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Food")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DailyOverviewCard(viewModel = viewModel)
            }

            item {
                MealTypeSelector(
                    selectedMealType = selectedMealType,
                    onMealTypeSelected = { selectedMealType = it }
                )
            }

            item {
                Text(
                    text = "Today's Intake",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            val intakeMap = viewModel.getTodayIntakesByMealType()
            intakeMap.forEach { (mealType, entries) ->
                item {
                    MealTypeCard(
                        mealType = mealType,
                        entries = entries,
                        onDeleteEntry = { entryId ->
                            viewModel.deleteCalorieIntake(entryId)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    if (showAddFoodDialog) {
        AddFoodDialog(
            viewModel = viewModel,
            selectedMealType = selectedMealType,
            onDismiss = { showAddFoodDialog = false },
            onCustomFood = {
                showAddFoodDialog = false
                showCustomFoodDialog = true
            }
        )
    }

    if (showCustomFoodDialog) {
        CustomFoodDialog(
            viewModel = viewModel,
            selectedMealType = selectedMealType,
            onDismiss = { showCustomFoodDialog = false }
        )
    }

    if (showGoalDialog) {
        GoalSettingsDialog(
            viewModel = viewModel,
            onDismiss = { showGoalDialog = false }
        )
    }
}


@Composable
fun DailyOverviewCard(
    viewModel: CalorieViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Daily Progress",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            val totalConsumed = viewModel.totalCaloriesConsumed
            val dailyGoal = uiState.dailyGoal?.targetCalories ?: viewModel.dailyCalorieGoal
            val progress = if (dailyGoal > 0) viewModel.calorieProgress else 0f

            // Circular Progress
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.size(120.dp),
                    strokeWidth = 8.dp,
                    color = if (viewModel.isGoalExceeded())
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${totalConsumed.toInt()}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/ ${dailyGoal.toInt()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "cal",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Remaining",
                    value = "${viewModel.getRemainingCalories().toInt()}",
                    color = MaterialTheme.colorScheme.primary
                )

                StatItem(
                    label = "Progress",
                    value = "${(progress * 100).toInt()}%",
                    color = if (viewModel.isGoalExceeded())
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun StatItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MealTypeSelector(
    selectedMealType: String,
    onMealTypeSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snacks")

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Add to Meal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mealTypes) { mealType ->
                    FilterChip(
                        onClick = { onMealTypeSelected(mealType) },
                        label = { Text(mealType) },
                        selected = selectedMealType == mealType,
                        leadingIcon = if (selectedMealType == mealType) {
                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                        } else null
                    )
                }
            }
        }
    }
}

// 5. MealTypeCard.kt
@Composable
fun MealTypeCard(
    mealType: String,
    entries: List<CaloriesIntakeEntry>,
    onDeleteEntry: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mealType,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "${entries.sumOf { it.calories }.toInt()} cal",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            entries.forEach { entry ->
                IntakeEntryItem(
                    entry = entry,
                    onDelete = { onDeleteEntry(entry.id) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun IntakeEntryItem(
    entry: CaloriesIntakeEntry,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = entry.foodName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )

            if (entry.portion > 0) {
                Text(
                    text = "${entry.portion.toInt()}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${entry.calories.toInt()} cal",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary
            )

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodDialog(
    viewModel: CalorieViewModel,
    selectedMealType: String,
    onDismiss: () -> Unit,
    onCustomFood: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFood by remember { mutableStateOf<FoodItem?>(null) }
    var grams by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            viewModel.searchFoodItems(searchQuery)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Food") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Search Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search food...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Search Results
                if (searchQuery.isNotBlank()) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = 200.dp)
                    ) {
                        items(uiState.searchResults) { food ->
                            FoodItemRow(
                                food = food,
                                isSelected = selectedFood == food,
                                onSelect = { selectedFood = food }
                            )
                        }
                    }
                }

                // Selected Food Details
                selectedFood?.let { food ->
                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = food.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${food.caloriesPer100g.toInt()} cal per 100g",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = grams,
                                onValueChange = { grams = it },
                                label = { Text("Grams") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            if (grams.isNotBlank()) {
                                val gramsValue = grams.toDoubleOrNull() ?: 0.0
                                val totalCalories = (food.caloriesPer100g * gramsValue) / 100.0

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Total: ${totalCalories.toInt()} calories",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedFood?.let { food ->
                        val gramsValue = grams.toDoubleOrNull() ?: 0.0
                        if (gramsValue > 0) {
                            viewModel.addCalorieIntake(food, gramsValue, selectedMealType)
                            onDismiss()
                        }
                    }
                },
                enabled = selectedFood != null && grams.toDoubleOrNull() != null && grams.toDoubleOrNull()!! > 0
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun FoodItemRow(
    food: FoodItem,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = food.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
            Text(
                text = "${food.caloriesPer100g.toInt()} cal/100g",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (isSelected) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// 7. CustomFoodDialog.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomFoodDialog(
    viewModel: CalorieViewModel,
    selectedMealType: String,
    onDismiss: () -> Unit
) {
    var foodName by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var grams by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Custom Food") },
        text = {
            Column {
                OutlinedTextField(
                    value = foodName,
                    onValueChange = { foodName = it },
                    label = { Text("Food Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Total Calories") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = grams,
                    onValueChange = { grams = it },
                    label = { Text("Portion (grams)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (foodName.isNotBlank() && calories.isNotBlank()) {
                        val caloriesValue = calories.toDoubleOrNull() ?: 0.0
                        val gramsValue = grams.toDoubleOrNull() ?: 0.0

                        if (caloriesValue > 0) {
                            viewModel.addCustomCalorieIntake(
                                foodName = foodName,
                                calories = caloriesValue,
                                portionGrams = gramsValue,
                                mealType = selectedMealType
                            )
                            onDismiss()
                        }
                    }
                },
                enabled = foodName.isNotBlank() &&
                        calories.isNotBlank() &&
                        calories.toDoubleOrNull() != null &&
                        calories.toDoubleOrNull()!! > 0
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// 8. GoalSettingsDialog.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalSettingsDialog(
    viewModel: CalorieViewModel,
    onDismiss: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var goalInput by remember {
        mutableStateOf((uiState.dailyGoal?.targetCalories ?: viewModel.dailyCalorieGoal).toString())
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Daily Calorie Goal") },
        text = {
            Column {
                Text(
                    text = "Set your daily calorie intake goal:",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = goalInput,
                    onValueChange = { goalInput = it },
                    label = { Text("Calories") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    suffix = { Text("cal") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    goalInput.toDoubleOrNull()?.let { newGoal ->
                        if (newGoal > 0) {
                            viewModel.updateDailyGoal(newGoal)
                            onDismiss()
                        }
                    }
                },
                enabled = goalInput.toDoubleOrNull() != null && goalInput.toDoubleOrNull()!! > 0
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// No changes needed. The screen will reflect the updated dailyCalorieGoal from the ViewModel.

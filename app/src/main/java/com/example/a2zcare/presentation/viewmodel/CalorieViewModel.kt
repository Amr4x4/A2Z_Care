package com.example.a2zcare.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a2zcare.domain.entities.CaloriesIntakeEntry
import com.example.a2zcare.domain.entities.DailyCaloriesGoal
import com.example.a2zcare.domain.entities.FoodCategory
import com.example.a2zcare.domain.entities.FoodItem
import com.example.a2zcare.domain.usecases.AddCaloriesIntakeUseCase
import com.example.a2zcare.domain.usecases.CalculateTargetsUseCase
import com.example.a2zcare.domain.usecases.DeleteCaloriesIntakeUseCase
import com.example.a2zcare.domain.usecases.GetDailyCaloriesIntakeUseCase
import com.example.a2zcare.domain.usecases.GetDailyCaloriesProgressUseCase
import com.example.a2zcare.domain.usecases.GetFoodItemsUseCase
import com.example.a2zcare.domain.usecases.InitializeFoodItemsUseCase
import com.example.a2zcare.domain.usecases.SearchFoodItemsUseCase
import com.example.a2zcare.domain.usecases.UpdateDailyCaloriesGoalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class ConsumedFood(
    val foodItem: FoodItem,
    val grams: Double
) {
    val totalCalories: Double
        get() = (foodItem.caloriesPer100g * grams) / 100.0
}

data class BurnedCalories(
    val activity: Activity,
    val durationMinutes: Int,
    val caloriesBurned: Double
)

data class Activity(
    val name: String,
    val metValue: Double
)

data class CalorieUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val dailyGoal: DailyCaloriesGoal? = null,
    val todayIntakes: List<CaloriesIntakeEntry> = emptyList(),
    val foodCategories: List<FoodCategory> = emptyList(),
    val availableFoodItems: List<FoodItem> = emptyList(),
    val searchResults: List<FoodItem> = emptyList(),
    val isSearching: Boolean = false
)

@HiltViewModel
class CalorieViewModel @Inject constructor(
    private val addCaloriesIntakeUseCase: AddCaloriesIntakeUseCase,
    private val getDailyCaloriesProgressUseCase: GetDailyCaloriesProgressUseCase,
    private val getDailyCaloriesIntakeUseCase: GetDailyCaloriesIntakeUseCase,
    private val updateDailyCaloriesGoalUseCase: UpdateDailyCaloriesGoalUseCase,
    private val deleteCaloriesIntakeUseCase: DeleteCaloriesIntakeUseCase,
    private val getFoodItemsUseCase: GetFoodItemsUseCase,
    private val searchFoodItemsUseCase: SearchFoodItemsUseCase,
    private val initializeFoodItemsUseCase: InitializeFoodItemsUseCase
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(CalorieUiState())
    val uiState: StateFlow<CalorieUiState> = _uiState.asStateFlow()

    // Legacy mutable state for backward compatibility
    var consumedFoods by mutableStateOf(listOf<ConsumedFood>())
        private set

    var burnedCalories by mutableStateOf(listOf<BurnedCalories>())
        private set

    var dailyCalorieGoal by mutableDoubleStateOf(2000.0)
        private set

    var userWeight by mutableDoubleStateOf(70.0) // kg
        private set

    // Current date for operations
    private val currentDate: String
        get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    // Computed properties
    val totalCaloriesConsumed: Double
        get() = _uiState.value.todayIntakes.sumOf { it.calories }

    val totalCaloriesBurned: Double
        get() = burnedCalories.sumOf { it.caloriesBurned }

    val netCalories: Double
        get() = totalCaloriesConsumed - totalCaloriesBurned

    val calorieProgress: Float
        get() {
            val goal = _uiState.value.dailyGoal?.targetCalories ?: dailyCalorieGoal
            return if (goal > 0) (totalCaloriesConsumed / goal).toFloat().coerceIn(0f, 1f) else 0f
        }

    init {
        initializeData()
    }

    private fun initializeData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Initialize food items
                initializeFoodItemsUseCase()

                // Load today's data
                loadTodayData()

                // Load food categories and items
                loadFoodItems()

                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to initialize data: ${e.message}"
                )
            }
        }
    }

    private suspend fun loadTodayData() {
        try {
            val dailyGoal = getDailyCaloriesProgressUseCase()
            val todayIntakes = getDailyCaloriesIntakeUseCase()

            _uiState.value = _uiState.value.copy(
                dailyGoal = dailyGoal,
                todayIntakes = todayIntakes
            )

            // Update legacy state
            dailyCalorieGoal = dailyGoal.targetCalories
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = "Failed to load today's data: ${e.message}"
            )
        }
    }

    private suspend fun loadFoodItems() {
        try {
            val foodItems = getFoodItemsUseCase()
            _uiState.value = _uiState.value.copy(
                availableFoodItems = foodItems
            )
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = "Failed to load food items: ${e.message}"
            )
        }
    }

    // Add calorie intake entry
    fun addCalorieIntake(
        foodItem: FoodItem,
        grams: Double,
        mealType: String? = null
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                addCaloriesIntakeUseCase(foodItem, grams, mealType)

                // Refresh today's data
                loadTodayData()

                // Update legacy state
                val consumed = ConsumedFood(foodItem, grams)
                consumedFoods = consumedFoods + consumed

                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to add calorie intake: ${e.message}"
                )
            }
        }
    }

    // Add calorie intake with custom food
    fun addCustomCalorieIntake(
        foodName: String,
        calories: Double,
        portionGrams: Double,
        mealType: String? = null
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                addCaloriesIntakeUseCase(foodName, calories, portionGrams, mealType)

                // Refresh today's data
                loadTodayData()

                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to add custom calorie intake: ${e.message}"
                )
            }
        }
    }

    // Delete calorie intake entry
    fun deleteCalorieIntake(entryId: Long) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                deleteCaloriesIntakeUseCase(entryId)

                // Refresh today's data
                loadTodayData()

                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to delete calorie intake: ${e.message}"
                )
            }
        }
    }

    // Update daily calorie goal
    fun updateDailyGoal(newGoal: Double) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                updateDailyCaloriesGoalUseCase(newGoal)

                // Refresh today's data
                loadTodayData()

                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to update daily goal: ${e.message}"
                )
            }
        }
    }

    // Search food items
    fun searchFoodItems(query: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isSearching = true)

                val searchResults = searchFoodItemsUseCase(query)

                _uiState.value = _uiState.value.copy(
                    searchResults = searchResults,
                    isSearching = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    error = "Failed to search food items: ${e.message}"
                )
            }
        }
    }

    // Get food items by category
    fun getFoodItemsByCategory(category: String) {
        viewModelScope.launch {
            try {
                val items = getFoodItemsUseCase(category)
                _uiState.value = _uiState.value.copy(
                    availableFoodItems = items
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to load food items by category: ${e.message}"
                )
            }
        }
    }

    // Refresh all data
    fun refreshData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                loadTodayData()
                loadFoodItems()
                _uiState.value = _uiState.value.copy(isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to refresh data: ${e.message}"
                )
            }
        }
    }

    // Clear error state
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    // Legacy methods for backward compatibility
    fun addConsumedFood(foodItem: FoodItem, grams: Double) {
        addCalorieIntake(foodItem, grams)
    }

    fun addBurnedCalories(activity: Activity, durationMinutes: Int) {
        val caloriesBurned = calculateBurnedCalories(activity.metValue, userWeight, durationMinutes)
        val burned = BurnedCalories(activity, durationMinutes, caloriesBurned)
        burnedCalories = burnedCalories + burned
    }

    private fun calculateBurnedCalories(metValue: Double, weightKg: Double, durationMinutes: Int): Double {
        return metValue * weightKg * (durationMinutes / 60.0)
    }

    fun removeConsumedFood(index: Int) {
        consumedFoods = consumedFoods.filterIndexed { i, _ -> i != index }
    }

    fun removeBurnedCalories(index: Int) {
        burnedCalories = burnedCalories.filterIndexed { i, _ -> i != index }
    }


    // Get today's intake entries grouped by meal type
    fun getTodayIntakesByMealType(): Map<String, List<CaloriesIntakeEntry>> {
        return _uiState.value.todayIntakes.groupBy { it.mealType ?: "Other" }
    }

    // Get calories consumed for specific meal type
    fun getCaloriesForMealType(mealType: String): Double {
        return _uiState.value.todayIntakes
            .filter { it.mealType == mealType }
            .sumOf { it.calories }
    }

    // Get remaining calories for the day
    fun getRemainingCalories(): Double {
        val goal = _uiState.value.dailyGoal?.targetCalories ?: dailyCalorieGoal
        return (goal - totalCaloriesConsumed).coerceAtLeast(0.0)
    }

    // Check if daily goal is exceeded
    fun isGoalExceeded(): Boolean {
        val goal = _uiState.value.dailyGoal?.targetCalories ?: dailyCalorieGoal
        return totalCaloriesConsumed > goal
    }

    // Get goal percentage
    fun getGoalPercentage(): Float {
        return _uiState.value.dailyGoal?.goalPercentage ?: (calorieProgress * 100)
    }

    // Set from onboarding or profile (call this from onboarding after calculation)
    fun applyCalculatedCaloriesTarget(target: Int) {
        setDailyCalorieTarget(target.toDouble())
    }

    fun setDailyCalorieTarget(target: Double) {
        dailyCalorieGoal = target
        // Optionally, persist this as the default for the user
    }
}
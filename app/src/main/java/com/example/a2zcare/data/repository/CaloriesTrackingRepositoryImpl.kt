package com.example.a2zcare.data.repository

import com.example.a2zcare.data.local.dao.CaloriesTrackingDao
import com.example.a2zcare.data.local.entity.DailyCaloriesGoalEntity
import com.example.a2zcare.data.mapper.toDomain
import com.example.a2zcare.data.mapper.toEntity
import com.example.a2zcare.domain.entities.CaloriesIntakeEntry
import com.example.a2zcare.domain.entities.DailyCaloriesGoal
import com.example.a2zcare.domain.entities.FoodCategory
import com.example.a2zcare.domain.entities.FoodItem
import com.example.a2zcare.domain.repository.CaloriesTrackingRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CaloriesTrackingRepositoryImpl @Inject constructor(
    private val caloriesDao: CaloriesTrackingDao
) : CaloriesTrackingRepository {

    override suspend fun addCaloriesIntake(entry: CaloriesIntakeEntry) {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date(entry.timestamp))
        caloriesDao.insertCaloriesIntake(entry.toEntity(date))
        updateDailyProgress(date)
    }

    override suspend fun getDailyIntake(date: String): List<CaloriesIntakeEntry> {
        return caloriesDao.getDailyIntakes(date).map { it.toDomain() }
    }

    override suspend fun updateDailyGoal(goal: DailyCaloriesGoal) {
        caloriesDao.insertOrUpdateGoal(goal.toEntity())
    }

    override suspend fun getWeeklyData(startDate: String, endDate: String): List<DailyCaloriesGoal> {
        // Get all intake entries for the week
        val weeklyIntakes = caloriesDao.getWeeklyIntakes(startDate, endDate)
        val dailyGoals = mutableListOf<DailyCaloriesGoal>()

        // Group by date and create daily goals
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val groupedIntakes = weeklyIntakes.groupBy { it.date }

        groupedIntakes.forEach { (date, intakes) ->
            val totalCalories = intakes.sumOf { it.calories }
            val existingGoal = caloriesDao.getDailyGoal(date)

            val dailyGoal = if (existingGoal != null) {
                existingGoal.copy(achievedCalories = totalCalories).toDomain()
            } else {
                DailyCaloriesGoalEntity(
                    date = date,
                    targetCalories = 2000.0, // Default target
                    achievedCalories = totalCalories
                ).toDomain()
            }
            dailyGoals.add(dailyGoal)
        }

        return dailyGoals
    }

    override suspend fun getDailyGoal(date: String): DailyCaloriesGoal {
        val existingGoal = caloriesDao.getDailyGoal(date)
        val totalCalories = caloriesDao.getTotalCaloriesForDate(date) ?: 0.0

        return if (existingGoal != null) {
            existingGoal.copy(achievedCalories = totalCalories).toDomain()
        } else {
            val defaultGoal = DailyCaloriesGoalEntity(
                date = date,
                targetCalories = 2000.0, // Default target
                achievedCalories = totalCalories
            )
            caloriesDao.insertOrUpdateGoal(defaultGoal)
            defaultGoal.toDomain()
        }
    }

    override suspend fun deleteIntakeEntry(id: Long) {
        caloriesDao.deleteIntakeEntry(id)
        // Update daily progress after deletion
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        updateDailyProgress(currentDate)
    }

    private suspend fun updateDailyProgress(date: String) {
        val totalCalories = caloriesDao.getTotalCaloriesForDate(date) ?: 0.0
        val existingGoal = caloriesDao.getDailyGoal(date)

        val updatedGoal = if (existingGoal != null) {
            existingGoal.copy(achievedCalories = totalCalories)
        } else {
            DailyCaloriesGoalEntity(
                date = date,
                targetCalories = 2000.0,
                achievedCalories = totalCalories
            )
        }
        caloriesDao.insertOrUpdateGoal(updatedGoal)
    }

    // Food items data - similar to your existing CalorieRepository
    private val foodCategories = listOf(
        FoodCategory(1, "Vegetables", "🥬"),
        FoodCategory(2, "Fruits", "🍎"),
        FoodCategory(3, "Grains", "🌾"),
        FoodCategory(4, "Proteins", "🥩"),
        FoodCategory(5, "Dairy", "🥛"),
        FoodCategory(6, "Sweets", "🍭"),
        FoodCategory(7, "Drinks", "🥤")
    )

    private val foodItems = listOf(
        // Vegetables
        FoodItem(1, "Broccoli", 34.0, foodCategories[0]),
        FoodItem(2, "Spinach", 23.0, foodCategories[0]),
        FoodItem(3, "Carrot", 41.0, foodCategories[0]),
        FoodItem(4, "Bell Pepper", 31.0, foodCategories[0]),

        // Fruits
        FoodItem(5, "Apple", 52.0, foodCategories[1]),
        FoodItem(6, "Banana", 89.0, foodCategories[1]),
        FoodItem(7, "Orange", 47.0, foodCategories[1]),
        FoodItem(8, "Grapes", 69.0, foodCategories[1]),

        // Grains
        FoodItem(9, "Rice (cooked)", 130.0, foodCategories[2]),
        FoodItem(10, "Bread", 265.0, foodCategories[2]),
        FoodItem(11, "Pasta (cooked)", 131.0, foodCategories[2]),
        FoodItem(12, "Oats", 389.0, foodCategories[2]),

        // Proteins
        FoodItem(13, "Chicken Breast", 165.0, foodCategories[3]),
        FoodItem(14, "Salmon", 208.0, foodCategories[3]),
        FoodItem(15, "Eggs", 155.0, foodCategories[3]),
        FoodItem(16, "Tofu", 76.0, foodCategories[3]),

        // Dairy
        FoodItem(17, "Milk", 42.0, foodCategories[4]),
        FoodItem(18, "Cheese", 402.0, foodCategories[4]),
        FoodItem(19, "Yogurt", 59.0, foodCategories[4]),

        // Sweets
        FoodItem(20, "Chocolate", 546.0, foodCategories[5]),
        FoodItem(21, "Ice Cream", 207.0, foodCategories[5]),
        FoodItem(22, "Cookies", 502.0, foodCategories[5]),

        // Drinks
        FoodItem(23, "Soda", 41.0, foodCategories[6]),
        FoodItem(24, "Orange Juice", 45.0, foodCategories[6]),
        FoodItem(25, "Coffee", 2.0, foodCategories[6])
    )

    override fun getFoodCategories(): List<FoodCategory> = foodCategories

    override fun getFoodItemsByCategory(category: String): List<FoodItem> =
        foodItems.filter { it.category.name.equals(category, ignoreCase = true) }

    override fun searchFoodItems(query: String): List<FoodItem> =
        if (query.isBlank()) foodItems
        else foodItems.filter { it.name.contains(query, ignoreCase = true) }

    override fun initializeFoodItems() {
        // No-op: foodItems are hardcoded in memory. If you want to persist them, implement DB insert here.
    }
}
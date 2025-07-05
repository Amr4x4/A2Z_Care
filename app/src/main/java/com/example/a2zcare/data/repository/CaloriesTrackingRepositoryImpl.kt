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
        // --- Vegetables ---
        FoodItem(1, "Broccoli", 34.0, foodCategories[0]),
        FoodItem(2, "Spinach", 23.0, foodCategories[0]),
        FoodItem(3, "Carrot", 41.0, foodCategories[0]),
        FoodItem(4, "Bell Pepper", 31.0, foodCategories[0]),
        FoodItem(5, "Kale", 49.0, foodCategories[0]),
        FoodItem(6, "Cucumber", 16.0, foodCategories[0]),
        FoodItem(7, "Tomato", 18.0, foodCategories[0]),
        FoodItem(8, "Onion", 40.0, foodCategories[0]),
        FoodItem(9, "Potato (boiled)", 87.0, foodCategories[0]),
        FoodItem(10, "Sweet Potato", 86.0, foodCategories[0]),

        // --- Fruits ---
        FoodItem(11, "Apple", 52.0, foodCategories[1]),
        FoodItem(12, "Banana", 89.0, foodCategories[1]),
        FoodItem(13, "Orange", 47.0, foodCategories[1]),
        FoodItem(14, "Grapes", 69.0, foodCategories[1]),
        FoodItem(15, "Strawberries", 32.0, foodCategories[1]),
        FoodItem(16, "Pineapple", 50.0, foodCategories[1]),
        FoodItem(17, "Watermelon", 30.0, foodCategories[1]),
        FoodItem(18, "Mango", 60.0, foodCategories[1]),
        FoodItem(19, "Blueberries", 57.0, foodCategories[1]),
        FoodItem(20, "Avocado", 160.0, foodCategories[1]),

        // --- Grains ---
        FoodItem(21, "Rice (cooked)", 130.0, foodCategories[2]),
        FoodItem(22, "Bread (white)", 265.0, foodCategories[2]),
        FoodItem(23, "Pasta (cooked)", 131.0, foodCategories[2]),
        FoodItem(24, "Oats", 389.0, foodCategories[2]),
        FoodItem(25, "Quinoa", 120.0, foodCategories[2]),
        FoodItem(26, "Barley", 354.0, foodCategories[2]),
        FoodItem(27, "Corn", 86.0, foodCategories[2]),
        FoodItem(28, "Couscous", 112.0, foodCategories[2]),
        FoodItem(29, "Brown Rice", 123.0, foodCategories[2]),
        FoodItem(30, "Whole Wheat Bread", 247.0, foodCategories[2]),

        // --- Proteins ---
        FoodItem(31, "Chicken Breast", 165.0, foodCategories[3]),
        FoodItem(32, "Salmon", 208.0, foodCategories[3]),
        FoodItem(33, "Eggs", 155.0, foodCategories[3]),
        FoodItem(34, "Tofu", 76.0, foodCategories[3]),
        FoodItem(35, "Beef Steak", 271.0, foodCategories[3]),
        FoodItem(36, "Tuna", 132.0, foodCategories[3]),
        FoodItem(37, "Shrimp", 99.0, foodCategories[3]),
        FoodItem(38, "Lentils", 116.0, foodCategories[3]),
        FoodItem(39, "Chickpeas", 164.0, foodCategories[3]),
        FoodItem(40, "Peanut Butter", 588.0, foodCategories[3]),

        // --- Dairy ---
        FoodItem(41, "Milk", 42.0, foodCategories[4]),
        FoodItem(42, "Cheese", 402.0, foodCategories[4]),
        FoodItem(43, "Yogurt", 59.0, foodCategories[4]),
        FoodItem(44, "Butter", 717.0, foodCategories[4]),
        FoodItem(45, "Cream Cheese", 342.0, foodCategories[4]),
        FoodItem(46, "Greek Yogurt", 59.0, foodCategories[4]),
        FoodItem(47, "Ice Cream", 207.0, foodCategories[4]),
        FoodItem(48, "Cottage Cheese", 98.0, foodCategories[4]),
        FoodItem(49, "Custard", 122.0, foodCategories[4]),

        // --- Sweets ---
        FoodItem(50, "Chocolate", 546.0, foodCategories[5]),
        FoodItem(51, "Cookies", 502.0, foodCategories[5]),
        FoodItem(52, "Donuts", 452.0, foodCategories[5]),
        FoodItem(53, "Cake (sponge)", 297.0, foodCategories[5]),
        FoodItem(54, "Brownies", 466.0, foodCategories[5]),
        FoodItem(55, "Pancakes", 227.0, foodCategories[5]),
        FoodItem(56, "Candy Bar", 488.0, foodCategories[5]),
        FoodItem(57, "Waffles", 291.0, foodCategories[5]),
        FoodItem(58, "Jam", 250.0, foodCategories[5]),
        FoodItem(59, "Honey", 304.0, foodCategories[5]),

        // --- Drinks ---
        FoodItem(60, "Soda", 41.0, foodCategories[6]),
        FoodItem(61, "Orange Juice", 45.0, foodCategories[6]),
        FoodItem(62, "Coffee", 2.0, foodCategories[6]),
        FoodItem(63, "Green Tea", 0.0, foodCategories[6]),
        FoodItem(64, "Milkshake", 120.0, foodCategories[6]),
        FoodItem(65, "Beer", 43.0, foodCategories[6]),
        FoodItem(66, "Wine", 85.0, foodCategories[6]),
        FoodItem(67, "Smoothie (fruit)", 90.0, foodCategories[6]),
        FoodItem(68, "Energy Drink", 110.0, foodCategories[6]),
        FoodItem(69, "Hot Chocolate", 77.0, foodCategories[6])
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
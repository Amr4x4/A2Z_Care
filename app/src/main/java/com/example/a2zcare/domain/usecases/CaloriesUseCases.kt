package com.example.a2zcare.domain.usecases

import com.example.a2zcare.domain.entities.CaloriesIntakeEntry
import com.example.a2zcare.domain.entities.DailyCaloriesGoal
import com.example.a2zcare.domain.entities.FoodItem
import com.example.a2zcare.domain.repository.CaloriesTrackingRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class AddCaloriesIntakeUseCase @Inject constructor(
    private val repository: CaloriesTrackingRepository
) {
    suspend operator fun invoke(
        foodItem: FoodItem,
        portionGrams: Double,
        mealType: String? = null
    ) {
        val calories = (foodItem.caloriesPer100g * portionGrams) / 100.0
        val entry = CaloriesIntakeEntry(
            timestamp = System.currentTimeMillis(),
            calories = calories,
            foodName = foodItem.name,
            portion = portionGrams,
            mealType = mealType
        )
        repository.addCaloriesIntake(entry)
    }

    suspend operator fun invoke(
        foodName: String,
        calories: Double,
        portionGrams: Double,
        mealType: String? = null
    ) {
        val entry = CaloriesIntakeEntry(
            timestamp = System.currentTimeMillis(),
            calories = calories,
            foodName = foodName,
            portion = portionGrams,
            mealType = mealType
        )
        repository.addCaloriesIntake(entry)
    }
}

class GetDailyCaloriesProgressUseCase @Inject constructor(
    private val repository: CaloriesTrackingRepository
) {
    suspend operator fun invoke(date: String = getCurrentDate()): DailyCaloriesGoal {
        return repository.getDailyGoal(date)
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}

class GetDailyCaloriesIntakeUseCase @Inject constructor(
    private val repository: CaloriesTrackingRepository
) {
    suspend operator fun invoke(date: String = getCurrentDate()): List<CaloriesIntakeEntry> {
        return repository.getDailyIntake(date)
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}

class UpdateDailyCaloriesGoalUseCase @Inject constructor(
    private val repository: CaloriesTrackingRepository
) {
    suspend operator fun invoke(targetCalories: Double, date: String = getCurrentDate()) {
        val currentGoal = repository.getDailyGoal(date)
        val updatedGoal = currentGoal.copy(targetCalories = targetCalories)
        repository.updateDailyGoal(updatedGoal)
    }

    private fun getCurrentDate(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}

class DeleteCaloriesIntakeUseCase @Inject constructor(
    private val repository: CaloriesTrackingRepository
) {
    suspend operator fun invoke(entryId: Long) {
        repository.deleteIntakeEntry(entryId)
    }
}

class GetFoodItemsUseCase @Inject constructor(
    private val repository: CaloriesTrackingRepository
) {
    suspend operator fun invoke(category: String? = null): List<FoodItem> {
        return if (category != null) {
            repository.getFoodItemsByCategory(category)
        } else {
            repository.searchFoodItems("")
        }
    }
}

class SearchFoodItemsUseCase @Inject constructor(
    private val repository: CaloriesTrackingRepository
) {
    suspend operator fun invoke(query: String): List<FoodItem> {
        return repository.searchFoodItems(query)
    }
}

class InitializeFoodItemsUseCase @Inject constructor(
    private val repository: CaloriesTrackingRepository
) {
    suspend operator fun invoke() {
        repository.initializeFoodItems()
    }
}

package com.example.a2zcare.domain.repository

import com.example.a2zcare.domain.entities.CaloriesIntakeEntry
import com.example.a2zcare.domain.entities.DailyCaloriesGoal
import com.example.a2zcare.domain.entities.FoodCategory
import com.example.a2zcare.domain.entities.FoodItem

interface CaloriesTrackingRepository {
    suspend fun addCaloriesIntake(entry: CaloriesIntakeEntry)
    suspend fun getDailyIntake(date: String): List<CaloriesIntakeEntry>
    suspend fun updateDailyGoal(goal: DailyCaloriesGoal)
    suspend fun getWeeklyData(startDate: String, endDate: String): List<DailyCaloriesGoal>
    suspend fun getDailyGoal(date: String): DailyCaloriesGoal
    suspend fun deleteIntakeEntry(id: Long)
    fun getFoodCategories(): List<FoodCategory>
    fun getFoodItemsByCategory(category: String): List<FoodItem>
    fun searchFoodItems(query: String): List<FoodItem>
    fun initializeFoodItems()
}
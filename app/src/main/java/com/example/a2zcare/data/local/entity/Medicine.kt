package com.example.a2zcare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "medicines")
data class Medicine(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val dose: String,
    val type: MedicineType,
    val remainingPills: Int,
    val totalPills: Int,
    val intakeTimes: List<String>, // Add this field that was missing
    val tips: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val finishedAt: Long? = null,
    val lowStockThreshold: Int = 5
)

@Entity(tableName = "medicine_schedules")
data class MedicineSchedule(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val medicineId: String,
    val timeHour: Int,
    val timeMinute: Int,
    val isActive: Boolean = true,
    val daysOfWeek: List<Int> = listOf(1, 2, 3, 4, 5, 6, 7) // 1=Monday, 7=Sunday
)

@Entity(tableName = "medicine_history")
data class MedicineHistory(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val medicineId: String,
    val takenAt: Long,
    val scheduledAt: Long,
    val dose: String,
    val wasOnTime: Boolean,
    val notes: String = ""
)

enum class MedicineType {
    PILLS, LIQUID, INJECTION, DROPS, INHALER
}
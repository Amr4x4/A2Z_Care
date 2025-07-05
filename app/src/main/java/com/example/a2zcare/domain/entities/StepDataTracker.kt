package com.example.a2zcare.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StepDataTracker(
    val id: String,
    val userId: String,
    val date: String,
    val steps: Int,
    val caloriesBurned: Int,
    val distanceKm: Float,
    val activeMinutes: Int,
    val lastUpdated: Long
) : Parcelable

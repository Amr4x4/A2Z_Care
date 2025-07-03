package com.example.a2zcare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_users")
data class SavedUserEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val targetUserId: String,
    val targetUserName: String,
    val targetUserEmail: String,
    val targetUserPhone: String?,
    val savedAt: Long
)

@Entity(tableName = "emergency_users")
data class EmergencyUserEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val targetUserId: String,
    val targetUserName: String,
    val targetUserEmail: String,
    val targetUserPhone: String?,
    val addedAt: Long
)
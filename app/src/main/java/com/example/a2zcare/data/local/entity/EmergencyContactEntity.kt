package com.example.a2zcare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_contacts")
data class EmergencyContactEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phoneNumber: String,
    val email: String?,
    val relation: String,
    val isPrimary: Boolean
)

data class EmergencyContact(
    val id: Int = 0,
    val name: String,
    val phoneNumber: String,
    val email: String? = null,
    val relation: String,
    val isPrimary: Boolean = false
)

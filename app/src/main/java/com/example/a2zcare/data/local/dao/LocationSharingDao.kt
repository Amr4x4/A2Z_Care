package com.example.a2zcare.data.local.dao

import androidx.room.*
import com.example.a2zcare.data.local.entity.EmergencyUserEntity
import com.example.a2zcare.data.local.entity.SavedUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedUserDao {
    @Query("SELECT * FROM saved_users WHERE userId = :userId ORDER BY savedAt DESC")
    fun getSavedUsers(userId: String): Flow<List<SavedUserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedUser(savedUser: SavedUserEntity)

    @Query("DELETE FROM saved_users WHERE userId = :userId AND targetUserId = :targetUserId")
    suspend fun deleteSavedUser(userId: String, targetUserId: String)

    @Query("DELETE FROM saved_users WHERE userId = :userId")
    suspend fun deleteAllSavedUsers(userId: String)
}

@Dao
interface EmergencyUserDao {
    @Query("SELECT * FROM emergency_users WHERE userId = :userId ORDER BY addedAt DESC")
    fun getEmergencyUsers(userId: String): Flow<List<EmergencyUserEntity>>

    @Query("SELECT * FROM emergency_users WHERE userId = :userId ORDER BY addedAt DESC")
    suspend fun getEmergencyUsersSync(userId: String): List<EmergencyUserEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmergencyUser(emergencyUser: EmergencyUserEntity)

    @Query("DELETE FROM emergency_users WHERE userId = :userId AND targetUserId = :targetUserId")
    suspend fun deleteEmergencyUser(userId: String, targetUserId: String)

    @Query("DELETE FROM emergency_users WHERE userId = :userId")
    suspend fun deleteAllEmergencyUsers(userId: String)
}

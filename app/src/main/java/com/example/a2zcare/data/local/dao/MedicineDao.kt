package com.example.a2zcare.data.local.dao

import androidx.room.*
import com.example.a2zcare.data.local.entity.Medicine
import com.example.a2zcare.data.local.entity.MedicineHistory
import com.example.a2zcare.data.local.entity.MedicineSchedule
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicines WHERE isActive = 1")
    fun getActiveMedicines(): Flow<List<Medicine>>

    @Query("SELECT * FROM medicines WHERE isActive = 0")
    fun getFinishedMedicines(): Flow<List<Medicine>>

    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getMedicineById(id: String): Medicine?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicine(medicine: Medicine)

    @Update
    suspend fun updateMedicine(medicine: Medicine)

    @Delete
    suspend fun deleteMedicine(medicine: Medicine)

    @Query("UPDATE medicines SET remainingPills = :pills WHERE id = :id")
    suspend fun updateRemainingPills(id: String, pills: Int)

    @Query("UPDATE medicines SET isActive = 0, finishedAt = :finishedAt WHERE id = :id")
    suspend fun markAsFinished(id: String, finishedAt: Long)
}

@Dao
interface MedicineScheduleDao {
    @Query("SELECT * FROM medicine_schedules WHERE medicineId = :medicineId AND isActive = 1")
    suspend fun getSchedulesForMedicine(medicineId: String): List<MedicineSchedule>

    @Query("SELECT * FROM medicine_schedules WHERE isActive = 1")
    fun getAllActiveSchedules(): Flow<List<MedicineSchedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: MedicineSchedule)

    @Update
    suspend fun updateSchedule(schedule: MedicineSchedule)

    @Delete
    suspend fun deleteSchedule(schedule: MedicineSchedule)

    @Query("DELETE FROM medicine_schedules WHERE medicineId = :medicineId")
    suspend fun deleteSchedulesForMedicine(medicineId: String)
}

@Dao
interface MedicineHistoryDao {
    @Query("SELECT * FROM medicine_history WHERE medicineId = :medicineId ORDER BY takenAt DESC")
    fun getHistoryForMedicine(medicineId: String): Flow<List<MedicineHistory>>

    @Query("SELECT * FROM medicine_history ORDER BY takenAt DESC")
    fun getAllHistory(): Flow<List<MedicineHistory>>

    @Insert
    suspend fun insertHistory(history: MedicineHistory)

    @Query("DELETE FROM medicine_history WHERE medicineId = :medicineId")
    suspend fun deleteHistoryForMedicine(medicineId: String)
}
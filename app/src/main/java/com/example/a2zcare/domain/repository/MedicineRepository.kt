package com.example.a2zcare.domain.repository

import com.example.a2zcare.data.local.dao.MedicineDao
import com.example.a2zcare.data.local.dao.MedicineHistoryDao
import com.example.a2zcare.data.local.dao.MedicineScheduleDao
import com.example.a2zcare.data.local.entity.Medicine
import com.example.a2zcare.data.local.entity.MedicineHistory
import com.example.a2zcare.data.local.entity.MedicineSchedule
import com.example.a2zcare.data.local.entity.MedicineType
import com.example.a2zcare.data.model.NextMedicineInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicineRepository @Inject constructor(
    private val medicineDao: MedicineDao,
    private val scheduleDao: MedicineScheduleDao,
    private val historyDao: MedicineHistoryDao
) {
    fun getActiveMedicines(): Flow<List<Medicine>> = medicineDao.getActiveMedicines()
    fun getFinishedMedicines(): Flow<List<Medicine>> = medicineDao.getFinishedMedicines()
    fun getAllActiveSchedules(): Flow<List<MedicineSchedule>> = scheduleDao.getAllActiveSchedules()

    suspend fun getMedicineById(id: String): Medicine? = medicineDao.getMedicineById(id)

    suspend fun insertMedicine(medicine: Medicine) {
        medicineDao.insertMedicine(medicine)

        // Create schedules for intake times
        medicine.intakeTimes.forEach { timeStr ->
            val timeParts = timeStr.split(":")
            if (timeParts.size == 2) {
                val hour = timeParts[0].toIntOrNull() ?: 0
                val minute = timeParts[1].toIntOrNull() ?: 0

                scheduleDao.insertSchedule(
                    MedicineSchedule(
                        medicineId = medicine.id,
                        timeHour = hour,
                        timeMinute = minute
                    )
                )
            }
        }
    }

    suspend fun updateMedicine(medicine: Medicine) {
        medicineDao.updateMedicine(medicine)

        // Update schedules
        scheduleDao.deleteSchedulesForMedicine(medicine.id)
        medicine.intakeTimes.forEach { timeStr ->
            val timeParts = timeStr.split(":")
            if (timeParts.size == 2) {
                val hour = timeParts[0].toIntOrNull() ?: 0
                val minute = timeParts[1].toIntOrNull() ?: 0

                scheduleDao.insertSchedule(
                    MedicineSchedule(
                        medicineId = medicine.id,
                        timeHour = hour,
                        timeMinute = minute
                    )
                )
            }
        }
    }

    suspend fun deleteMedicine(medicine: Medicine) {
        medicineDao.deleteMedicine(medicine)
        scheduleDao.deleteSchedulesForMedicine(medicine.id)
        historyDao.deleteHistoryForMedicine(medicine.id)
    }

    suspend fun takeMedicine(medicineId: String, scheduledTime: String) {
        val medicine = medicineDao.getMedicineById(medicineId) ?: return

        // Update remaining pills only if it's a pill type
        if (medicine.type == MedicineType.PILLS) {
            val newRemainingPills = medicine.remainingPills - 1
            medicineDao.updateRemainingPills(medicineId, newRemainingPills)

            // Check if medicine is finished
            if (newRemainingPills <= 0) {
                medicineDao.markAsFinished(medicineId, System.currentTimeMillis())
            }
        }

        // Add to history
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val scheduledTimeMillis = try {
            val today = Calendar.getInstance()
            val scheduleTime = timeFormat.parse(scheduledTime)
            scheduleTime?.let {
                val cal = Calendar.getInstance()
                cal.time = it
                today.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY))
                today.set(Calendar.MINUTE, cal.get(Calendar.MINUTE))
                today.timeInMillis
            } ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }

        historyDao.insertHistory(
            MedicineHistory(
                medicineId = medicineId,
                takenAt = System.currentTimeMillis(),
                scheduledAt = scheduledTimeMillis,
                dose = medicine.dose,
                wasOnTime = Math.abs(System.currentTimeMillis() - scheduledTimeMillis) < 30 * 60 * 1000 // 30 minutes tolerance
            )
        )
    }

    suspend fun getNextMedicine(): NextMedicineInfo? {
        val activeMedicines = medicineDao.getActiveMedicines().first()
        if (activeMedicines.isEmpty()) return null

        val currentTime = Calendar.getInstance()
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        var nextMedicine: Medicine? = null
        var nextTimeMillis: Long = Long.MAX_VALUE
        var nextTimeString: String = ""

        activeMedicines.forEach { medicine ->
            medicine.intakeTimes.forEach { timeStr ->
                try {
                    val scheduleTime = Calendar.getInstance().apply {
                        val time = timeFormat.parse(timeStr)
                        time?.let {
                            set(Calendar.HOUR_OF_DAY, it.hours)
                            set(Calendar.MINUTE, it.minutes)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)

                            // If time has passed today, schedule for tomorrow
                            if (before(currentTime)) {
                                add(Calendar.DAY_OF_YEAR, 1)
                            }
                        }
                    }

                    if (scheduleTime.timeInMillis < nextTimeMillis) {
                        nextTimeMillis = scheduleTime.timeInMillis
                        nextMedicine = medicine
                        nextTimeString = timeStr
                    }
                } catch (e: Exception) {
                    // Handle parsing error
                }
            }
        }

        return nextMedicine?.let { medicine ->
            NextMedicineInfo(
                medicine = medicine,
                nextTime = nextTimeMillis,
                remainingTimeMillis = nextTimeMillis - currentTime.timeInMillis
            )
        }
    }

    fun getMedicineHistory(medicineId: String): Flow<List<MedicineHistory>> {
        return historyDao.getHistoryForMedicine(medicineId)
    }

    suspend fun getMedicinesByTime(time: String): List<Medicine> {
        val schedules = scheduleDao.getAllActiveSchedules().first()
        val matchingSchedules = schedules.filter {
            val formatted = String.format("%02d:%02d", it.timeHour, it.timeMinute)
            formatted == time
        }

        val medicines = mutableListOf<Medicine>()
        for (schedule in matchingSchedules) {
            val medicine = medicineDao.getMedicineById(schedule.medicineId)
            if (medicine != null) {
                medicines.add(medicine)
            }
        }

        return medicines
    }


    fun getAllHistory(): Flow<List<MedicineHistory>> {
        return historyDao.getAllHistory()
    }
}
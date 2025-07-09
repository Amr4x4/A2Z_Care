package com.example.a2zcare.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.a2zcare.di.WorkerAssistedFactory
import com.example.a2zcare.domain.repository.MedicineRepository
import com.example.a2zcare.presentation.screens.notification.MedicineNotificationManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*

class MedicineReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MedicineRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("MedicineReminderWorker", "Starting medicine reminder check...")

            val currentTime = getCurrentTimeRoundedToNearestMinute()
            Log.d("MedicineReminderWorker", "Current time: $currentTime")

            // Get all active schedules
            val schedules = repository.getAllActiveSchedules().first()
            Log.d("MedicineReminderWorker", "Found ${schedules.size} active schedules")

            // Filter by time
            val medicinesToRemind = schedules.filter { schedule ->
                val formatted = String.format("%02d:%02d", schedule.timeHour, schedule.timeMinute)
                formatted == currentTime
            }.mapNotNull { schedule ->
                repository.getMedicineById(schedule.medicineId)
            }

            Log.d("MedicineReminderWorker", "Found ${medicinesToRemind.size} medicines to remind")

            // Show notifications
            val notificationManager = MedicineNotificationManager(applicationContext)
            medicinesToRemind.forEach { medicine ->
                Log.d("MedicineReminderWorker", "Showing notification for: ${medicine.name}")
                notificationManager.showMedicineReminder(medicine, currentTime)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("MedicineReminderWorker", "Failed to fetch/send reminders", e)
            Result.failure()
        }
    }

    private fun getCurrentTimeRoundedToNearestMinute(): String {
        val now = Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(now.time)
    }

    @AssistedFactory
    interface Factory : WorkerAssistedFactory<MedicineReminderWorker> {
        override fun create(context: Context, params: WorkerParameters): MedicineReminderWorker
    }
}
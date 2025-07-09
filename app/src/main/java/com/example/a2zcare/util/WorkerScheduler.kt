// Unified WorkerScheduler.kt
package com.example.a2zcare.util

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.a2zcare.service.MedicineReminderWorker
import java.util.concurrent.TimeUnit

object WorkerScheduler {

    private const val MEDICINE_REMINDER_WORK_NAME = "medicine_reminder_work"

    fun scheduleMedicineReminderWorker(context: Context) {
        Log.d("WorkerScheduler", "Scheduling medicine reminder worker...")

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresBatteryNotLow(false) // Allow on low battery for important reminders
            .setRequiresCharging(false)
            .setRequiresDeviceIdle(false)
            .build()

        // Use 1 minute interval for testing, change to 15 minutes for production
        val request = PeriodicWorkRequestBuilder<MedicineReminderWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setInitialDelay(1, TimeUnit.MINUTES) // Start after 1 minute
            .build()

        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniquePeriodicWork(
            MEDICINE_REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, // This ensures fresh scheduling
            request
        )

        Log.d("WorkerScheduler", "Medicine reminder worker scheduled successfully")
    }

    fun cancelMedicineReminderWorker(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(MEDICINE_REMINDER_WORK_NAME)
        Log.d("WorkerScheduler", "Medicine reminder worker cancelled")
    }

    fun checkWorkerStatus(context: Context) {
        val workManager = WorkManager.getInstance(context)
        val workInfosLiveData = workManager.getWorkInfosForUniqueWorkLiveData(MEDICINE_REMINDER_WORK_NAME)

        workInfosLiveData.observeForever { workInfos ->
            if (workInfos.isNotEmpty()) {
                val workInfo = workInfos[0]
                Log.d("WorkerScheduler", "Worker status: ${workInfo.state}")
                if (workInfo.state == WorkInfo.State.FAILED) {
                    Log.e("WorkerScheduler", "Worker failed, rescheduling...")
                    scheduleMedicineReminderWorker(context)
                }
            } else {
                Log.w("WorkerScheduler", "No worker found, scheduling...")
                scheduleMedicineReminderWorker(context)
            }
        }
    }
}
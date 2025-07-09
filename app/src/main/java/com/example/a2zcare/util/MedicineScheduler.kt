// File: com.example.a2zcare.util.MedicineScheduler.kt

package com.example.a2zcare.util

import android.content.Context
import androidx.work.*
import com.example.a2zcare.service.MedicineReminderWorker
import java.util.concurrent.TimeUnit

class MedicineScheduler(private val context: Context) {

    fun schedulePeriodicReminders() {
        val workManager = WorkManager.getInstance(context)

        val workRequest = PeriodicWorkRequestBuilder<MedicineReminderWorker>(
            1, TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            MEDICINE_REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    companion object {
        const val MEDICINE_REMINDER_WORK_NAME = "medicine_reminder_work"
    }
}

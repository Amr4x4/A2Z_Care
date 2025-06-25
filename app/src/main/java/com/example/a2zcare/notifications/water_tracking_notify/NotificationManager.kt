package com.example.a2zcare.notifications.water_tracking_notify


import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.a2zcare.MyApplication.Companion.WATER_REMINDER_WORK_NAME
import com.example.a2zcare.presentation.screens.notification.WaterReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun scheduleWaterReminder(intervalMinutes: Int) {
        val workRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(
            intervalMinutes.toLong(), TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WATER_REMINDER_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun cancelWaterReminder() {
        WorkManager.getInstance(context).cancelUniqueWork(WATER_REMINDER_WORK_NAME)
    }
}

package com.example.a2zcare

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Configuration
import com.example.a2zcare.di.HiltWorkerFactory
import com.example.a2zcare.presentation.model.LiveStatusManager
import com.example.a2zcare.presentation.screens.notification.MedicineNotificationManager
import com.example.a2zcare.service.StepCounterService
import com.example.a2zcare.util.WorkerScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(Log.DEBUG) // Add logging for debugging
            .build()

    override fun onCreate() {
        super.onCreate()
        Log.d("MyApplication", "App created")

        // Initialize notification channels first
        createAllNotificationChannels()

        // Initialize LiveStatusManager
        LiveStatusManager.init(this)

        // Schedule medicine reminders
        scheduleMedicineReminders()

        // Start step tracking if permission granted
        if (hasActivityRecognitionPermission()) {
            startStepTrackingService()
        }
    }

    private fun createAllNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Medicine reminder channels
            val medicineChannel = NotificationChannel(
                MedicineNotificationManager.CHANNEL_ID,
                MedicineNotificationManager.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for medicine reminders"
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }

            val lowStockChannel = NotificationChannel(
                MedicineNotificationManager.LOW_STOCK_CHANNEL_ID,
                MedicineNotificationManager.LOW_STOCK_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for low medicine stock"
                setShowBadge(true)
            }

            // Water reminder channel
            val waterChannel = NotificationChannel(
                WATER_REMINDER_CHANNEL_ID,
                "Water Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications to remind you to drink water"
            }

            manager.createNotificationChannel(medicineChannel)
            manager.createNotificationChannel(lowStockChannel)
            manager.createNotificationChannel(waterChannel)

            Log.d("MyApplication", "All notification channels created")
        }
    }

    private fun scheduleMedicineReminders() {
        try {
            WorkerScheduler.scheduleMedicineReminderWorker(this)
            Log.d("MyApplication", "Medicine reminders scheduled successfully")
        } catch (e: Exception) {
            Log.e("MyApplication", "Failed to schedule medicine reminders", e)
        }
    }

    private fun hasActivityRecognitionPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    private fun startStepTrackingService() {
        val intent = Intent(this, StepCounterService::class.java).apply {
            action = StepCounterService.ACTION_START_SERVICE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    companion object {
        const val WATER_REMINDER_CHANNEL_ID = "water_reminder_channel"
        const val WATER_REMINDER_WORK_NAME = "water_reminder_work"
    }
}
package com.example.a2zcare.presentation.screens.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.a2zcare.R

class ReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun doWork(): Result {
        createNotificationChannel()
        val builder = NotificationCompat.Builder(applicationContext, "hydration_channel")
            .setSmallIcon(R.drawable.app_main_icon)
            .setContentTitle("Time to drink water!")
            .setContentText("Stay hydrated!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        NotificationManagerCompat.from(applicationContext).notify(System.currentTimeMillis().toInt(), builder.build())

        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "hydration_channel",
                "Hydration Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders to drink water"
            }
            val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}

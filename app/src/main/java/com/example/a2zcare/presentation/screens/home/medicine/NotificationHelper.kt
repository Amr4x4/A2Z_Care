package com.example.a2zcare.presentation.screens.home.medicine

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.a2zcare.R
import com.example.a2zcare.data.local.entity.Medicine

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "medicine_reminders"
        const val CHANNEL_NAME = "Medicine Reminders"
        const val LOW_STOCK_CHANNEL_ID = "low_stock_alerts"
        const val LOW_STOCK_CHANNEL_NAME = "Low Stock Alerts"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val medicineChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for medicine reminders"
                enableVibration(true)
                enableLights(true)
            }

            val lowStockChannel = NotificationChannel(
                LOW_STOCK_CHANNEL_ID,
                LOW_STOCK_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for low medicine stock"
            }

            notificationManager.createNotificationChannel(medicineChannel)
            notificationManager.createNotificationChannel(lowStockChannel)
        }
    }

    fun showMedicineReminder(medicine: Medicine) {
        val takenIntent = Intent(context, MedicineActionReceiver::class.java).apply {
            action = "MARK_AS_TAKEN"
            putExtra("medicine_id", medicine.id)
        }

        val snoozeIntent = Intent(context, MedicineActionReceiver::class.java).apply {
            action = "SNOOZE_REMINDER"
            putExtra("medicine_id", medicine.id)
        }

        val takenPendingIntent = PendingIntent.getBroadcast(
            context,
            medicine.id.hashCode(),
            takenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            medicine.id.hashCode() + 1,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Time to take your medicine!")
            .setContentText("${medicine.name} - ${medicine.dose}")
            .setSmallIcon(R.drawable.medicine_check)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 1000))
            .addAction(
                R.drawable.check,
                "Mark as Taken",
                takenPendingIntent
            )
            .addAction(
                R.drawable.snooze,
                "Snooze 15min",
                snoozePendingIntent
            )
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("${medicine.name} - ${medicine.dose}\n${medicine.tips}")
            )
            .build()

        notificationManager.notify(medicine.id.hashCode(), notification)
    }

    fun showLowStockAlert(medicine: Medicine) {
        val notification = NotificationCompat.Builder(context, LOW_STOCK_CHANNEL_ID)
            .setContentTitle("Low stock alert!")
            .setContentText("${medicine.name} - Only ${medicine.remainingPills} pills left")
            .setSmallIcon(R.drawable.warning)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            medicine.id.hashCode() + 1000,
            notification
        )
    }
}
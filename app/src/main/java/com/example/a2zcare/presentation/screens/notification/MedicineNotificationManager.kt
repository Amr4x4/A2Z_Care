// Fixed MedicineNotificationManager.kt
package com.example.a2zcare.presentation.screens.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.a2zcare.R
import com.example.a2zcare.data.local.entity.Medicine
import com.example.a2zcare.presentation.screens.home.medicine.MedicineActionReceiver

class MedicineNotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "medicine_reminders"
        const val CHANNEL_NAME = "Medicine Reminders"
        const val LOW_STOCK_CHANNEL_ID = "low_stock_alerts"
        const val LOW_STOCK_CHANNEL_NAME = "Low Stock Alerts"
    }

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
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
            }

            val lowStockChannel = NotificationChannel(
                LOW_STOCK_CHANNEL_ID,
                LOW_STOCK_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for low medicine stock"
                setShowBadge(true)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(medicineChannel)
            manager.createNotificationChannel(lowStockChannel)

            Log.d("NotificationManager", "Notification channels created")
        }
    }

    fun showMedicineReminder(medicine: Medicine, scheduledTime: String) {
        Log.d("NotificationManager", "Attempting to show notification for: ${medicine.name}")

        if (!hasNotificationPermission()) {
            Log.w("NotificationManager", "No notification permission granted")
            return
        }

        val takenIntent = Intent(context, MedicineActionReceiver::class.java).apply {
            action = "MARK_AS_TAKEN"
            putExtra("medicine_id", medicine.id)
            putExtra("scheduled_time", scheduledTime)
        }

        val snoozeIntent = Intent(context, MedicineActionReceiver::class.java).apply {
            action = "SNOOZE_REMINDER"
            putExtra("medicine_id", medicine.id)
            putExtra("scheduled_time", scheduledTime)
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
            .setContentTitle("💊 Time to take your medicine!")
            .setContentText("${medicine.name} - ${medicine.dose}")
            .setSmallIcon(R.drawable.medicine_check)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .apply {
                if (medicine.tips.isNotEmpty()) {
                    setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("${medicine.name} - ${medicine.dose}\n💡 ${medicine.tips}")
                    )
                }
            }
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
            .build()

        try {
            NotificationManagerCompat.from(context).notify(medicine.id.hashCode(), notification)
            Log.d("NotificationManager", "Notification shown successfully for: ${medicine.name}")
        } catch (e: SecurityException) {
            Log.e("NotificationManager", "Security exception showing notification", e)
        }
    }

    fun showLowStockAlert(medicine: Medicine) {
        if (!hasNotificationPermission()) return

        val notification = NotificationCompat.Builder(context, LOW_STOCK_CHANNEL_ID)
            .setContentTitle("⚠️ Low Medicine Stock")
            .setContentText("${medicine.name} has only ${medicine.remainingPills} pills left")
            .setSmallIcon(R.drawable.warning)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(medicine.id.hashCode() + 1000, notification)
            Log.d("NotificationManager", "Low stock notification shown for: ${medicine.name}")
        } catch (e: SecurityException) {
            Log.e("NotificationManager", "Security exception showing low stock notification", e)
        }
    }

    fun cancelNotification(medicineId: String) {
        if (!hasNotificationPermission()) return

        try {
            NotificationManagerCompat.from(context).cancel(medicineId.hashCode())
        } catch (e: SecurityException) {
            Log.e("NotificationManager", "Security exception cancelling notification", e)
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }
}
package com.example.a2zcare.presentation.screens.home.medicine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.a2zcare.service.MedicineNotificationWorker
import com.example.a2zcare.domain.repository.MedicineRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MedicineActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: MedicineRepository

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "MARK_AS_TAKEN" -> {
                val medicineId = intent.getStringExtra("medicine_id") ?: return
                val scheduledTime = intent.getStringExtra("scheduled_time") ?: return

                CoroutineScope(Dispatchers.IO).launch {
                    repository.takeMedicine(medicineId, scheduledTime)
                }
            }

            "SNOOZE_REMINDER" -> {
                val medicineId = intent.getStringExtra("medicine_id") ?: return
                val scheduledTime = intent.getStringExtra("scheduled_time") ?: return
                scheduleSnoozeNotification(context, medicineId, scheduledTime)
            }
        }
    }

    private fun scheduleSnoozeNotification(context: Context, medicineId: String, scheduledTime: String) {
        val workManager = WorkManager.getInstance(context)

        val snoozeRequest = OneTimeWorkRequestBuilder<MedicineNotificationWorker>()
            .setInitialDelay(15, TimeUnit.MINUTES)
            .setInputData(
                workDataOf(
                    "medicine_id" to medicineId,
                    "scheduled_time" to scheduledTime
                )
            )
            .build()

        workManager.enqueue(snoozeRequest)
    }
}

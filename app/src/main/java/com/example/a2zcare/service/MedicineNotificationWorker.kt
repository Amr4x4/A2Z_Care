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

class MedicineNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: MedicineRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val medicines = repository.getActiveMedicines().first()
            val currentTime = getCurrentFormattedTime()
            val notifier = MedicineNotificationManager(applicationContext)

            for (medicine in medicines) {
                if (medicine.intakeTimes.any { timeMatches(it, currentTime) }) {
                    notifier.showMedicineReminder(medicine, currentTime)
                }

                if (medicine.type.name == "PILLS" && medicine.remainingPills in 1..5) {
                    notifier.showLowStockAlert(medicine)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("MedicineNotificationWorker", "Error during notification check", e)
            Result.failure()
        }
    }

    private fun timeMatches(intakeTime: String, currentTime: String): Boolean {
        return try {
            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
            val intake = format.parse(intakeTime)
            val current = format.parse(currentTime)
            val diff = kotlin.math.abs(current!!.time - intake!!.time)
            diff <= 60 * 1000
        } catch (e: Exception) {
            false
        }
    }

    private fun getCurrentFormattedTime(): String {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(Date())
    }

    @AssistedFactory
    interface Factory : WorkerAssistedFactory<MedicineNotificationWorker> {
        override fun create(context: Context, params: WorkerParameters): MedicineNotificationWorker
    }

}

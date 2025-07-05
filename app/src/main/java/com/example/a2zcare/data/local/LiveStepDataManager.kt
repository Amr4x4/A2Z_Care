package com.example.a2zcare.data.local

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.a2zcare.domain.entities.StepDataTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveStepDataManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val _currentStepData = MutableLiveData<StepDataTracker>()
    val currentStepData: LiveData<StepDataTracker> = _currentStepData

    // Add StateFlow for Compose compatibility
    private val _stepDataFlow = MutableStateFlow<StepDataTracker?>(null)
    val stepDataFlow: StateFlow<StepDataTracker?> = _stepDataFlow.asStateFlow()

    private val localBroadcastManager = LocalBroadcastManager.getInstance(context)
    private var isReceiverRegistered = false

    companion object {
        const val ACTION_STEP_UPDATE = "com.example.a2zcare.STEP_UPDATE"
        const val EXTRA_STEP_DATA = "step_data"
        const val EXTRA_DAILY_STEPS = "daily_steps"
        const val EXTRA_CALORIES = "calories"
        const val EXTRA_DISTANCE = "distance"
        const val EXTRA_ACTIVE_MINUTES = "active_minutes"
    }

    private val stepUpdateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_STEP_UPDATE) {
                try {
                    val stepData = intent.getParcelableExtra<StepDataTracker>(EXTRA_STEP_DATA)
                    if (stepData != null) {
                        updateStepData(stepData)
                    } else {
                        // Fallback to individual values
                        val steps = intent.getIntExtra(EXTRA_DAILY_STEPS, 0)
                        val calories = intent.getIntExtra(EXTRA_CALORIES, 0)
                        val distance = intent.getFloatExtra(EXTRA_DISTANCE, 0f)
                        val activeMinutes = intent.getIntExtra(EXTRA_ACTIVE_MINUTES, 0)
                        val newStepData = StepDataTracker(
                            id = "user_default_${getCurrentDate()}",
                            userId = "user_default",
                            date = getCurrentDate(),
                            steps = steps,
                            caloriesBurned = calories,
                            distanceKm = distance,
                            activeMinutes = activeMinutes,
                            lastUpdated = System.currentTimeMillis()
                        )
                        updateStepData(newStepData)
                    }

                    Log.d("LiveStepDataManager", "Step data updated via broadcast")
                } catch (e: Exception) {
                    Log.e("LiveStepDataManager", "Error processing step update", e)
                }
            }
        }
    }

    init {
        startListening()
    }

    private fun startListening() {
        if (!isReceiverRegistered) {
            val intentFilter = IntentFilter(ACTION_STEP_UPDATE)
            localBroadcastManager.registerReceiver(stepUpdateReceiver, intentFilter)
            isReceiverRegistered = true
            Log.d("LiveStepDataManager", "Broadcast receiver registered")
        }
    }

    fun updateStepData(stepData: StepDataTracker) {
        _currentStepData.postValue(stepData)
        _stepDataFlow.value = stepData
        Log.d("LiveStepDataManager", "Step data updated: ${stepData.steps} steps")
    }

    fun getCurrentStepData(): StepDataTracker? {
        return _currentStepData.value
    }

    private fun getCurrentDate(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    fun cleanup() {
        if (isReceiverRegistered) {
            try {
                localBroadcastManager.unregisterReceiver(stepUpdateReceiver)
                isReceiverRegistered = false
            } catch (e: Exception) {
                Log.e("LiveStepDataManager", "Error unregistering receiver", e)
            }
        }
    }
}
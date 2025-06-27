package com.example.a2zcare.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import com.example.a2zcare.MainActivity
import com.example.a2zcare.R
import com.example.a2zcare.domain.entities.ActivityLevel
import com.example.a2zcare.domain.entities.UserProfile
import com.example.a2zcare.domain.repository.StepTrackerRepository
import com.example.a2zcare.domain.usecases.StepTrackingUseCase
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@SuppressLint("MissingPermission")
class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var notificationManager: NotificationManager? = null

    @Inject
    lateinit var repository: StepTrackerRepository

    @Inject
    lateinit var stepTrackingUseCase: StepTrackingUseCase

    private var lastStepCount = 0
    private var dailySteps = 0
    private var lastActivityTime = System.currentTimeMillis()
    private var userProfile: UserProfile? = null

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "step_tracker_channel"
        const val INACTIVITY_THRESHOLD = 60 * 60 * 1000L // 1 hour
        const val ACTION_START_SERVICE = "START_SERVICE"
        const val ACTION_STOP_SERVICE = "STOP_SERVICE"
    }

    override fun onCreate() {
        super.onCreate()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        loadUserProfile()

        stepCounterSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
        }

        startForeground(NOTIFICATION_ID, createNotification())
        startInactivityChecker()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_SERVICE -> {
                startForeground(NOTIFICATION_ID, createNotification())
            }
            ACTION_STOP_SERVICE -> {
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {
                val totalSteps = it.values[0].toInt()

                if (lastStepCount == 0) {
                    lastStepCount = totalSteps
                    loadTodaySteps()
                }

                dailySteps = totalSteps - lastStepCount
                lastActivityTime = System.currentTimeMillis()

                // Update step count in repository
                updateStepCount()

                // Update notification
                notificationManager?.notify(NOTIFICATION_ID, createNotification())
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes if needed
    }

    private fun loadUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            userProfile = repository.getUserProfile()
        }
    }

    private fun loadTodaySteps() {
        CoroutineScope(Dispatchers.IO).launch {
            val today = getCurrentDate()
            val todayData = repository.getStepDataByDate(today)
            todayData?.let {
                dailySteps = it.steps
            }
        }
    }

    private fun updateStepCount() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                stepTrackingUseCase.updateStepCount(dailySteps)
            } catch (e: Exception) {
                Log.e("StepCounterService", "Error updating step count", e)
            }
        }
    }

    private fun startInactivityChecker() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                checkInactivity()
                handler.postDelayed(this, 30 * 60 * 1000L) // Check every 30 minutes
            }
        }
        handler.post(runnable)
    }

    private fun checkInactivity() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastActivityTime > getInactivityThreshold()) {
            sendInactivityNotification()
        }
    }

    private fun getInactivityThreshold(): Long {
        return when (userProfile?.activityLevel) {
            ActivityLevel.SEDENTARY -> 45 * 60 * 1000L // 45 minutes
            ActivityLevel.LIGHTLY_ACTIVE -> 60 * 60 * 1000L // 1 hour
            ActivityLevel.MODERATELY_ACTIVE -> 90 * 60 * 1000L // 1.5 hours
            ActivityLevel.VERY_ACTIVE -> 120 * 60 * 1000L // 2 hours
            ActivityLevel.EXTREMELY_ACTIVE -> 150 * 60 * 1000L // 2.5 hours
            else -> INACTIVITY_THRESHOLD
        }
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
    private fun sendInactivityNotification() {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val motivationalMessages = listOf(
            "Time to Move! 🚶‍♂️",
            "Take a quick walk! 🌟",
            "Your body needs movement! 💪",
            "Step up your game! 👟",
            "Health is wealth - take some steps! 💚"
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(motivationalMessages.random())
            .setContentText("You've been inactive. Take a walk to reach your daily goal!")
            .setSmallIcon(R.drawable.walk)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager?.notify(2, notification)
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
    private fun createNotification(): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val progress = userProfile?.let { profile ->
            if (profile.dailyStepsTarget > 0) {
                (dailySteps.toFloat() / profile.dailyStepsTarget * 100).toInt()
            } else 0
        } ?: 0

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Step Tracker")
            .setContentText("$dailySteps steps today • ${progress}% of goal")
            .setSmallIcon(R.drawable.walk)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(100, progress, false)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Step Tracker",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows your daily step progress"
                enableLights(false)
                enableVibration(false)
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
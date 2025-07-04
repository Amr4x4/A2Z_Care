package com.example.a2zcare.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.PowerManager
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.a2zcare.MainActivity
import com.example.a2zcare.R
import com.example.a2zcare.domain.entities.StepDataTracker
import com.example.a2zcare.data.database.StepTrackerDatabase
import com.example.a2zcare.data.local.LiveStepDataManager
import com.example.a2zcare.data.local.entity.StepDataEntity
import com.example.a2zcare.data.local.entity.UserProfileEntity
import com.example.a2zcare.domain.usecases.CalculateTargetsUseCase
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class StepCounterService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var stepCounterSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null
    private var notificationManager: NotificationManager? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var database: StepTrackerDatabase
    private lateinit var localBroadcastManager: LocalBroadcastManager

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    private var initialStepCount = 0
    private var currentDailySteps = 0
    private var lastActivityTime = System.currentTimeMillis()
    private var userProfile: UserProfileEntity? = null
    private var isInitialized = false
    private var wakeLock: PowerManager.WakeLock? = null

    // Handler for immediate UI updates
    private val mainHandler = Handler(Looper.getMainLooper())

    @Inject
    lateinit var calculateTargetsUseCase: CalculateTargetsUseCase

    @Inject
    lateinit var liveStepDataManager: LiveStepDataManager

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "step_tracker_channel"
        const val PREFS_NAME = "step_tracker_prefs"
        const val KEY_INITIAL_STEPS = "initial_steps"
        const val KEY_DAILY_STEPS = "daily_steps"
        const val KEY_LAST_DATE = "last_date"
        const val INACTIVITY_THRESHOLD = 60 * 60 * 1000L // 1 hour
        const val ACTION_START_SERVICE = "START_SERVICE"
        const val ACTION_STOP_SERVICE = "STOP_SERVICE"

        // Broadcast actions
        const val ACTION_STEP_UPDATE = "com.example.a2zcare.STEP_UPDATE"
        const val EXTRA_STEP_DATA = "step_data"
        const val EXTRA_DAILY_STEPS = "daily_steps"
        const val EXTRA_CALORIES = "calories"
        const val EXTRA_DISTANCE = "distance"
        const val EXTRA_ACTIVE_MINUTES = "active_minutes"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("StepCounterService", "Service created")

        acquireWakeLock()
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        initializeService()
        setupSensors()
        createNotificationChannel()
        loadUserProfile()

        startForeground(NOTIFICATION_ID, createNotification())
        startInactivityChecker()
    }

    private fun initializeService() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        database = StepTrackerDatabase.getDatabase(this)

        // Check if we need to reset for new day
        checkAndResetForNewDay()
    }

    private fun setupSensors() {
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        // Use faster sensor delay for more responsive updates
        val sensorDelay = SensorManager.SENSOR_DELAY_UI // More responsive than SENSOR_DELAY_NORMAL

        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, sensorDelay)
            Log.d("StepCounterService", "Step counter sensor registered")
        } else if (stepDetectorSensor != null) {
            sensorManager.registerListener(this, stepDetectorSensor, sensorDelay)
            Log.d("StepCounterService", "Step detector sensor registered")
        } else {
            Log.e("StepCounterService", "No step sensors available")
        }
    }

    private fun checkAndResetForNewDay() {
        val today = getCurrentDate()
        val lastDate = sharedPreferences.getString(KEY_LAST_DATE, "")

        if (lastDate != today) {
            // New day - reset daily steps
            currentDailySteps = 0
            initialStepCount = 0
            isInitialized = false

            sharedPreferences.edit().apply {
                putString(KEY_LAST_DATE, today)
                putInt(KEY_DAILY_STEPS, 0)
                putInt(KEY_INITIAL_STEPS, 0)
                apply()
            }

            Log.d("StepCounterService", "Reset for new day: $today")
        } else {
            // Same day - load saved values
            currentDailySteps = sharedPreferences.getInt(KEY_DAILY_STEPS, 0)
            initialStepCount = sharedPreferences.getInt(KEY_INITIAL_STEPS, 0)
            Log.d("StepCounterService", "Loaded existing data: $currentDailySteps steps")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        serviceJob.cancel()
        releaseWakeLock()
        Log.d("StepCounterService", "Service destroyed")
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            "A2ZCare:StepCounterWakeLock"
        )
        wakeLock?.acquire()
    }

    private fun releaseWakeLock() {
        wakeLock?.let {
            if (it.isHeld) it.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_SERVICE -> {
                Log.d("StepCounterService", "Start service action")
                startForeground(NOTIFICATION_ID, createNotification())
            }
            ACTION_STOP_SERVICE -> {
                Log.d("StepCounterService", "Stop service action")
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    handleStepCounter(it.values[0].toInt())
                }
                Sensor.TYPE_STEP_DETECTOR -> {
                    handleStepDetector()
                }
            }
        }
    }

    private fun handleStepCounter(totalSteps: Int) {
        if (!isInitialized) {
            initialStepCount = totalSteps - currentDailySteps
            isInitialized = true

            sharedPreferences.edit().apply {
                putInt(KEY_INITIAL_STEPS, initialStepCount)
                apply()
            }

            Log.d("StepCounterService", "Initialized with total steps: $totalSteps, initial: $initialStepCount")
        }

        val newDailySteps = totalSteps - initialStepCount
        if (newDailySteps >= 0 && newDailySteps != currentDailySteps) {
            currentDailySteps = newDailySteps
            updateStepDataLive()
            Log.d("StepCounterService", "Updated steps: $currentDailySteps")
        }
    }

    private fun handleStepDetector() {
        currentDailySteps++
        updateStepDataLive()
        Log.d("StepCounterService", "Step detected, total: $currentDailySteps")
    }

    private fun updateStepDataLive() {
        lastActivityTime = System.currentTimeMillis()

        // Save to SharedPreferences immediately
        sharedPreferences.edit().apply {
            putInt(KEY_DAILY_STEPS, currentDailySteps)
            apply()
        }

        // Calculate stats
        val calories = calculateCaloriesBurned(currentDailySteps)
        val distance = calculateDistance(currentDailySteps)
        val activeMinutes = calculateActiveMinutes(currentDailySteps)

        // Update notification immediately on main thread
        mainHandler.post {
            try {
                notificationManager?.notify(NOTIFICATION_ID, createNotification())
            } catch (e: Exception) {
                Log.e("StepCounterService", "Error updating notification", e)
            }
        }

        // Prepare step data for broadcast and DB
        val today = getCurrentDate()
        val userId = "user_default"
        val stepDataId = "${userId}_$today"
        val stepDataTracker = StepDataTracker(
            id = stepDataId,
            userId = userId,
            date = today,
            steps = currentDailySteps,
            caloriesBurned = calories,
            distanceKm = distance,
            activeMinutes = activeMinutes,
            lastUpdated = System.currentTimeMillis()
        )

        // Send immediate broadcast for UI update (as Parcelable)
        sendImmediateBroadcast(stepDataTracker)

        // Update database and live data manager in background
        serviceScope.launch {
            try {
                val stepData = StepDataEntity(
                    id = stepDataId,
                    userId = userId,
                    date = today,
                    steps = currentDailySteps,
                    caloriesBurned = calories,
                    distanceKm = distance,
                    activeMinutes = activeMinutes,
                    lastUpdated = System.currentTimeMillis()
                )
                database.stepCounterDao().insertOrUpdate(stepData)
                withContext(Dispatchers.Main) {
                    liveStepDataManager.updateStepData(stepDataTracker)
                }
            } catch (e: Exception) {
                Log.e("StepCounterService", "Error updating step data", e)
            }
        }
    }

    private fun sendImmediateBroadcast(stepData: StepDataTracker) {
        val intent = Intent(ACTION_STEP_UPDATE).apply {
            putExtra(EXTRA_STEP_DATA, stepData)
            putExtra(EXTRA_DAILY_STEPS, stepData.steps)
            putExtra(EXTRA_CALORIES, stepData.caloriesBurned)
            putExtra(EXTRA_DISTANCE, stepData.distanceKm)
            putExtra(EXTRA_ACTIVE_MINUTES, stepData.activeMinutes)
        }
        try {
            localBroadcastManager.sendBroadcast(intent)
            sendBroadcast(intent)
            Log.d("StepCounterService", "Immediate broadcast sent for ${stepData.steps} steps")
        } catch (e: Exception) {
            Log.e("StepCounterService", "Error sending immediate broadcast", e)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("StepCounterService", "Sensor accuracy changed: $accuracy")
    }

    private fun loadUserProfile() {
        serviceScope.launch {
            try {
                userProfile = database.userProfileDao().getUserProfile()
                Log.d("StepCounterService", "User profile loaded: ${userProfile?.dailyStepsTarget}")
            } catch (e: Exception) {
                Log.e("StepCounterService", "Error loading user profile", e)
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
        val activityLevelEnum = try {
            userProfile?.activityLevel?.let {
                com.example.a2zcare.domain.entities.ActivityLevel.valueOf(it)
            }
        } catch (e: Exception) {
            null
        }

        return when (activityLevelEnum) {
            com.example.a2zcare.domain.entities.ActivityLevel.SEDENTARY -> 45 * 60 * 1000L
            com.example.a2zcare.domain.entities.ActivityLevel.LIGHTLY_ACTIVE -> 60 * 60 * 1000L
            com.example.a2zcare.domain.entities.ActivityLevel.MODERATELY_ACTIVE -> 90 * 60 * 1000L
            com.example.a2zcare.domain.entities.ActivityLevel.VERY_ACTIVE -> 120 * 60 * 1000L
            com.example.a2zcare.domain.entities.ActivityLevel.EXTREMELY_ACTIVE -> 150 * 60 * 1000L
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

        val targetSteps = try {
            userProfile?.let { calculateTargetsUseCase.getStepTarget(it) } ?: 10000
        } catch (e: Exception) {
            10000
        }

        val progress = if (targetSteps > 0) {
            (currentDailySteps.toFloat() / targetSteps * 100).toInt()
        } else 0

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Step Tracker")
            .setContentText("$currentDailySteps steps today • ${progress}% of goal")
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

    private fun calculateCaloriesBurned(steps: Int): Int {
        return (steps * 0.04).toInt()
    }

    private fun calculateDistance(steps: Int): Float {
        return (steps * 0.762f) / 1000f
    }

    private fun calculateActiveMinutes(steps: Int): Int {
        return steps / 100
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
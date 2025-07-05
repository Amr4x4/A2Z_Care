package com.example.a2zcare

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.compose.rememberNavController
import com.example.a2zcare.presentation.navegation.SetupNavGraph
import com.example.a2zcare.presentation.theme.A2ZCareTheme
import com.example.a2zcare.presentation.viewmodel.SplashScreenViewModel
import com.example.a2zcare.service.StepCounterService
import com.example.a2zcare.util.StepServiceManager
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalAnimationApi
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var splashViewModel: SplashScreenViewModel

    private lateinit var stepServiceManager: StepServiceManager

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            startStepTracking()
        } else {
            // Handle permission denied
            Log.w("MainActivity", "Required permissions not granted")
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startStepTrackingService()
        } else {
            // Handle permission denied
            Toast.makeText(this, "Permission required for step tracking", Toast.LENGTH_LONG).show()
        }
    }

    private val requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Optional: handle permission results here
    }

    private fun getRequiredPermissions(): Array<String> {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        return permissions.toTypedArray()
    }

    private fun hasAllPermissions(): Boolean {
        return getRequiredPermissions().all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }
    private lateinit var stepBroadcastReceiver: BroadcastReceiver
    private lateinit var localBroadcastManager: LocalBroadcastManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        setupStepBroadcastReceiver()
        stepServiceManager = StepServiceManager(this)
        requestPermissionsAndStartService()

        if (!hasAllPermissions()) {
            requestPermissionsLauncher.launch(getRequiredPermissions())
        }

        enableEdgeToEdge()

        installSplashScreen().setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }

        setContent {
            A2ZCareTheme {
                SetTopBarColor(MaterialTheme.colors.background)
                val startDestination by splashViewModel.startDestination
                val navController = rememberNavController()
                SetupNavGraph(navController = navController, startDestination = startDestination)
            }
        }
    }

    @Composable
    private fun SetTopBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(color = color)
        }
    }
    private fun setupStepBroadcastReceiver() {
        stepBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == StepCounterService.ACTION_STEP_UPDATE) {
                    val steps = intent.getIntExtra(StepCounterService.EXTRA_DAILY_STEPS, 0)
                    val calories = intent.getIntExtra(StepCounterService.EXTRA_CALORIES, 0)
                    val distance = intent.getFloatExtra(StepCounterService.EXTRA_DISTANCE, 0f)
                    val activeMinutes = intent.getIntExtra(StepCounterService.EXTRA_ACTIVE_MINUTES, 0)

                    // Update your UI immediately
                    runOnUiThread {
                        updateStepUI(steps, calories, distance, activeMinutes)
                    }

                    Log.d("MainActivity", "Received step update: $steps steps")
                }
            }
        }
    }
    private fun updateStepUI(steps: Int, calories: Int, distance: Float, activeMinutes: Int) {
        // Update your UI components here
        // For example, if you're using Compose:
        // stepViewModel.updateSteps(steps, calories, distance, activeMinutes)

        // Or if using traditional views:
        // stepCountTextView.text = steps.toString()
        // caloriesTextView.text = calories.toString()
        // distanceTextView.text = String.format("%.2f km", distance)
        // activeMinutesTextView.text = activeMinutes.toString()
    }


    private fun requestPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACTIVITY_RECOGNITION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            } else {
                startStepTrackingService()
            }
        } else {
            startStepTrackingService()
        }
    }
    private fun startStepTrackingService() {
        val serviceIntent = Intent(this, StepCounterService::class.java).apply {
            action = StepCounterService.ACTION_START_SERVICE
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }
    private fun requestPermissionsAndStartService() {
        val requiredPermissions = stepServiceManager.getRequiredPermissions()
        val missingPermissions = requiredPermissions.filter { permission ->
            ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            permissionLauncher.launch(missingPermissions.toTypedArray())
        } else {
            startStepTracking()
        }
    }

    private fun startStepTracking() {
        if (stepServiceManager.startStepTracking()) {
            Log.d("MainActivity", "Step tracking service started")
        } else {
            Log.w("MainActivity", "Failed to start step tracking service")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
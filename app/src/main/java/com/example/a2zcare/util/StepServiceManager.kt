package com.example.a2zcare.util

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.a2zcare.service.StepCounterService

class StepServiceManager(private val context: Context) {

    fun startStepTracking(): Boolean {
        if (!hasRequiredPermissions()) {
            return false
        }

        if (!isServiceRunning()) {
            val intent = Intent(context, StepCounterService::class.java).apply {
                action = StepCounterService.ACTION_START_SERVICE
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
            return true
        }
        return false
    }

    fun stopStepTracking() {
        val intent = Intent(context, StepCounterService::class.java).apply {
            action = StepCounterService.ACTION_STOP_SERVICE
        }
        context.stopService(intent)
    }

    fun isServiceRunning(): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        @Suppress("DEPRECATION")
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (StepCounterService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun hasRequiredPermissions(): Boolean {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            permissions.add(Manifest.permission.FOREGROUND_SERVICE)
            permissions.add(Manifest.permission.FOREGROUND_SERVICE_HEALTH)
        }

        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun getRequiredPermissions(): List<String> {
        val permissions = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            permissions.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            permissions.add(Manifest.permission.FOREGROUND_SERVICE)
            permissions.add(Manifest.permission.FOREGROUND_SERVICE_HEALTH)
        }

        return permissions
    }
}
package com.example.a2zcare.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object TrackingPreferences {
    private val Context.dataStore by preferencesDataStore("tracking_prefs")

    private val TRACKING_KEY = booleanPreferencesKey("is_tracking")

    suspend fun saveTrackingState(context: Context, isTracking: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[TRACKING_KEY] = isTracking
        }
    }

    val trackingFlow: (Context) -> Flow<Boolean> = { context ->
        context.dataStore.data
            .map { prefs -> prefs[TRACKING_KEY] ?: true }
    }
}

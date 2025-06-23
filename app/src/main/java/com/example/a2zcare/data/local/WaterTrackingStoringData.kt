package com.example.a2zcare.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.a2zcare.domain.repository.WaterTrackingRepo
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("waterTracking_pref")

class WaterTrackingStoringData(private val context: Context) : WaterTrackingRepo {

    private val GLASS_COUNT_KEY = intPreferencesKey("glass_count")
    private val INTERVAL_MINUTE_KEY = intPreferencesKey("interval_minute")

    override suspend fun getGlassCount(): Int = context.dataStore.data.first()[GLASS_COUNT_KEY] ?: 0

    override suspend fun incrementGlassCount() {
        context.dataStore.edit { it[GLASS_COUNT_KEY] = getGlassCount() + 1 }
    }

    override suspend fun getReminderInterval(): Int = context.dataStore.data.first()[INTERVAL_MINUTE_KEY] ?: 0

    override suspend fun setReminderInterval(minute: Int) {
        context.dataStore.edit { it[INTERVAL_MINUTE_KEY] = minute }
    }
}

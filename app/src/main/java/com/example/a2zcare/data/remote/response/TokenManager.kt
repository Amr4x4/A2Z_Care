package com.example.a2zcare.data.remote.response

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val DEVICE_ID_KEY = stringPreferencesKey("device_id")
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun saveDeviceId(deviceId: String) {
        dataStore.edit { preferences ->
            preferences[DEVICE_ID_KEY] = deviceId
        }
    }

    fun getToken(): String? {
        return runBlocking {
            dataStore.data.first()[TOKEN_KEY]
        }
    }

    fun getUserId(): String? {
        return runBlocking {
            dataStore.data.first()[USER_ID_KEY]
        }
    }

    fun getDeviceId(): String? {
        return runBlocking {
            dataStore.data.first()[DEVICE_ID_KEY]
        }
    }

    suspend fun clearAllTokens() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] != null
        }
    }
}
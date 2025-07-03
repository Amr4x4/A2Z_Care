package com.example.a2zcare.data.remote.response

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.a2zcare.data.model.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_preferences")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private val dataStore = context.dataStore

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_DATA_KEY = stringPreferencesKey("user_data")
        private val DEVICE_ID_KEY = stringPreferencesKey("device_id")
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String? {
        return dataStore.data.first()[TOKEN_KEY]
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun getUserId(): String? {
        return dataStore.data.first()[USER_ID_KEY]
    }

    suspend fun saveUserData(user: User) {
        dataStore.edit { preferences ->
            preferences[USER_DATA_KEY] = gson.toJson(user)
            preferences[USER_ID_KEY] = user.id
        }
    }

    suspend fun getUserData(): User? {
        val userJson = dataStore.data.first()[USER_DATA_KEY]
        return userJson?.let { gson.fromJson(it, User::class.java) }
    }

    suspend fun saveDeviceId(deviceId: String) {
        dataStore.edit { preferences ->
            preferences[DEVICE_ID_KEY] = deviceId
        }
    }

    suspend fun getDeviceId(): String? {
        return dataStore.data.first()[DEVICE_ID_KEY]
    }

    suspend fun clearAllTokens() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            !preferences[TOKEN_KEY].isNullOrEmpty() && !preferences[USER_ID_KEY].isNullOrEmpty()
        }
    }

    fun getCurrentUserId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[USER_ID_KEY]
        }
    }

    fun getCurrentUserData(): Flow<User?> {
        return dataStore.data.map { preferences ->
            val userJson = preferences[USER_DATA_KEY]
            userJson?.let { gson.fromJson(it, User::class.java) }
        }
    }

    suspend fun getUserFirstName(): String {
        val userData = getUserData()
        return when {
            !userData?.firstName.isNullOrBlank() -> userData!!.firstName!!
            !userData?.name.isNullOrBlank() -> userData!!.name!!
            !userData?.userName.isNullOrBlank() -> userData!!.userName!!
            else -> "User"
        }
    }

    fun getCurrentUserFirstName(): Flow<String> {
        return dataStore.data.map { preferences ->
            val userJson = preferences[USER_DATA_KEY]
            val userData = userJson?.let { gson.fromJson(it, User::class.java) }
            when {
                !userData?.firstName.isNullOrBlank() -> userData.firstName!!
                !userData?.name.isNullOrBlank() -> userData.name!!
                !userData?.userName.isNullOrBlank() -> userData.userName!!
                else -> "User"
            }
        }
    }
}

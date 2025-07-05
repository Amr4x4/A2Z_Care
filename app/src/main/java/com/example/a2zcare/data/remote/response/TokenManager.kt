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
        return try {
            dataStore.data.first()[TOKEN_KEY]
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun getUserId(): String? {
        return try {
            dataStore.data.first()[USER_ID_KEY]
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveUserData(user: User) {
        dataStore.edit { preferences ->
            preferences[USER_DATA_KEY] = gson.toJson(user)
            preferences[USER_ID_KEY] = user.id
        }
    }

    suspend fun getUserEmail(): String? {
        return try {
            val userData = getUserData()
            userData?.email
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserData(): User? {
        return try {
            val userJson = dataStore.data.first()[USER_DATA_KEY]
            userJson?.let {
                try {
                    gson.fromJson(it, User::class.java)
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveDeviceId(deviceId: String) {
        dataStore.edit { preferences ->
            preferences[DEVICE_ID_KEY] = deviceId
        }
    }

    suspend fun getDeviceId(): String? {
        return try {
            dataStore.data.first()[DEVICE_ID_KEY]
        } catch (e: Exception) {
            null
        }
    }

    suspend fun clearAllTokens() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    fun isLoggedIn(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            try {
                !preferences[TOKEN_KEY].isNullOrEmpty() && !preferences[USER_ID_KEY].isNullOrEmpty()
            } catch (e: Exception) {
                false
            }
        }
    }

    fun getCurrentUserId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            try {
                preferences[USER_ID_KEY]
            } catch (e: Exception) {
                null
            }
        }
    }

    fun getCurrentUserData(): Flow<User?> {
        return dataStore.data.map { preferences ->
            try {
                val userJson = preferences[USER_DATA_KEY]
                userJson?.let {
                    try {
                        gson.fromJson(it, User::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    suspend fun getUserFirstName(): String {
        return try {
            val userData = getUserData()
            when {
                !userData?.firstName.isNullOrBlank() -> userData!!.firstName!!
                !userData?.name.isNullOrBlank() -> userData!!.name!!
                !userData?.userName.isNullOrBlank() -> userData!!.userName!!
                else -> "User"
            }
        } catch (e: Exception) {
            "User"
        }
    }

    fun getCurrentUserFirstName(): Flow<String> {
        return dataStore.data.map { preferences ->
            try {
                val userJson = preferences[USER_DATA_KEY]
                val userData = userJson?.let {
                    try {
                        gson.fromJson(it, User::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }
                when {
                    !userData?.firstName.isNullOrBlank() -> userData.firstName!!
                    !userData?.name.isNullOrBlank() -> userData.name!!
                    !userData?.userName.isNullOrBlank() -> userData.userName!!
                    else -> "User"
                }
            } catch (e: Exception) {
                "User"
            }
        }
    }

    fun getCurrentUserEmail(): Flow<String?> {
        return dataStore.data.map { preferences ->
            try {
                val userJson = preferences[USER_DATA_KEY]
                val userData = userJson?.let {
                    try {
                        gson.fromJson(it, User::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }
                userData?.email
            } catch (e: Exception) {
                null
            }
        }
    }
}
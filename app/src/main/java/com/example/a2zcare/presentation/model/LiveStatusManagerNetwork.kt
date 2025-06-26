package com.example.a2zcare.presentation.model

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

class LiveStatusManager private constructor(private val context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: LiveStatusManager? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = LiveStatusManager(context.applicationContext)
                    }
                }
            }
        }

        val isOnline: StateFlow<Boolean>
            get() = INSTANCE?._isOnline ?: MutableStateFlow(false)

        val lastSeen: StateFlow<Long>
            get() = INSTANCE?._lastSeen ?: MutableStateFlow(System.currentTimeMillis())

        fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

        fun unregister() {
            INSTANCE?.unregisterCallback()
        }
    }

    private val _isOnline = MutableStateFlow(true)
    private val _lastSeen = MutableStateFlow(System.currentTimeMillis())

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isOnline.value = true
        }

        override fun onLost(network: Network) {
            _isOnline.value = false
            _lastSeen.value = System.currentTimeMillis()
        }

        override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
            val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            if (hasInternet && !_isOnline.value) {
                _isOnline.value = true
            } else if (!hasInternet && _isOnline.value) {
                _isOnline.value = false
                _lastSeen.value = System.currentTimeMillis()
            }
        }
    }

    init {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        // Set initial state
        _isOnline.value = isCurrentlyConnected()
        if (!_isOnline.value) {
            _lastSeen.value = System.currentTimeMillis()
        }
    }

    private fun isCurrentlyConnected(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun unregisterCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}

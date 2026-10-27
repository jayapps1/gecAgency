package com.ghanaemergencyconnect.agency.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class NetworkStatus {
    Available,
    Unavailable,
    Losing
}

class ConnectivityObserver(context: Context) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _status = MutableStateFlow(NetworkStatus.Available)
    val status: StateFlow<NetworkStatus> = _status.asStateFlow()

    init {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _status.value = NetworkStatus.Available
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                _status.value = NetworkStatus.Losing
            }

            override fun onLost(network: Network) {
                _status.value = NetworkStatus.Unavailable
            }
        }

        try {
            val activeNetwork = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            if (capabilities == null || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                _status.value = NetworkStatus.Unavailable
            }

            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } catch (e: Exception) {
            _status.value = NetworkStatus.Available
        }
    }

    val isOnline: Boolean
        get() = _status.value == NetworkStatus.Available
}

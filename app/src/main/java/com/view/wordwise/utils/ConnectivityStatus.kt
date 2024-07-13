package com.view.wordwise.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

enum class ConnectivityStatus {
    AVAILABLE,
    UNAVAILABLE,
    LOST
}

class ConnectivityObserver(val context: Context) {

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun observeConnectivity(): Flow<ConnectivityStatus> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                trySend(ConnectivityStatus.AVAILABLE)
            }

            override fun onLost(network: android.net.Network) {
                trySend(ConnectivityStatus.LOST)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        } else {
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                    val isConnected = activeNetwork?.isConnected == true
                    if (isConnected) {
                        trySend(ConnectivityStatus.AVAILABLE)
                    } else {
                        trySend(ConnectivityStatus.UNAVAILABLE)
                    }
                }
            }, filter)
        }

        awaitClose {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }
    }
}

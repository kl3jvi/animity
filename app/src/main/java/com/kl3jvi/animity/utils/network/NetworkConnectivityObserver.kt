package com.kl3jvi.animity.utils.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkConnectivityObserver @Inject constructor(
    private val connectivityManager: ConnectivityManager
) : ConnectivityObserver {

    /**
     * We create a callbackFlow that will emit a boolean value whenever the network state changes
     *
     * @return A Flow of Boolean values.
     */
    override fun observe(): Flow<Boolean> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    launch {
                        send(true)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    launch {
                        send(false)
                    }
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    launch {
                        send(false)
                    }
                }
            }
            connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)
            awaitClose { connectivityManager.unregisterNetworkCallback(callback) }
        }.distinctUntilChanged()
    }
}

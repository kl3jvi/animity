package com.kl3jvi.animity.utils.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.os.Build
import androidx.annotation.RequiresApi

object ConnectedCompat {
    private val IMPL: ConnectedCompatImpl

    init {
        IMPL =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                MarshMallowImpl
            } else {
                BaseImpl
            }
    }

    fun isConnected(connectivityManager: ConnectivityManager) = IMPL.isConnected(connectivityManager)

    internal interface ConnectedCompatImpl {
        fun isConnected(connectivityManager: ConnectivityManager): Boolean
    }

    object BaseImpl : ConnectedCompatImpl {
        @Suppress("DEPRECATION")
        override fun isConnected(connectivityManager: ConnectivityManager): Boolean =
            connectivityManager.activeNetworkInfo?.isConnected ?: false
    }

    object MarshMallowImpl : ConnectedCompatImpl {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun isConnected(connectivityManager: ConnectivityManager): Boolean =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?.hasCapability(NET_CAPABILITY_INTERNET) == true
    }
}

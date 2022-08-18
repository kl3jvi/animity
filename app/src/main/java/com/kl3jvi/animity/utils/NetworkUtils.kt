package com.kl3jvi.animity.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.kl3jvi.animity.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.net.InetAddress


/**
 * Network Utility to detect availability or unavailability of Internet connection
 */
object NetworkUtils : ConnectivityManager.NetworkCallback() {

    private val networkLiveData: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * > We register a network callback with the connectivity manager, and then we retrieve the current
     * status of connectivity
     *
     * @param context Context - The context of the activity or fragment
     * @return A LiveData object that is being observed by the MainActivity.
     */
    private fun getNetworkLiveData(context: Context): LiveData<Boolean> {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(this)
        } else {
            val builder = NetworkRequest.Builder()
            connectivityManager.registerNetworkCallback(builder.build(), this)
        }

        var isConnected = false

        // Retrieve current status of connectivity
        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }

        networkLiveData.postValue(isConnected)

        return networkLiveData
    }


    /**
     * It posts a value to the networkLiveData object.
     *
     * @param network The Network object that has become available.
     */
    override fun onAvailable(network: Network) {
        networkLiveData.postValue(true)
    }

    /**
     * It posts a value to the networkLiveData object.
     *
     * @param network The Network object that has been lost.
     */
    override fun onLost(network: Network) {
        networkLiveData.postValue(false)
    }

    /* An extension function that is being used to observe the networkLiveData object. */
    fun Context.isConnectedToInternet(owner: LifecycleOwner, observer: (Boolean) -> Unit) {
        getNetworkLiveData(context = this).observe(owner) {
            observer(it)
        }
    }
}

internal fun OkHttpClient.Builder.addChuckerOnDebug(
    chuckerInterceptor: ChuckerInterceptor
) = apply {
    if (BuildConfig.DEBUG) {
        addInterceptor(chuckerInterceptor)
    }
}


fun OkHttpClient.Builder.addGoogleDns() = (
        addGenericDns(
            "https://dns.google/dns-query",
            listOf(
                "8.8.4.4",
                "8.8.8.8"
            )
        ))

fun OkHttpClient.Builder.addCloudFlareDns() = (
        addGenericDns(
            "https://cloudflare-dns.com/dns-query",
            listOf(
                "1.1.1.1",
                "1.0.0.1",
                "2606:4700:4700::1111",
                "2606:4700:4700::1001"
            )
        ))

fun OkHttpClient.Builder.addAdGuardDns() = (
        addGenericDns(
            "https://dns.adguard.com/dns-query",
            listOf(
                // "Non-filtering"
                "94.140.14.140",
                "94.140.14.141",
            )
        ))

fun OkHttpClient.Builder.addGenericDns(url: String, ips: List<String>) = dns(
    DnsOverHttps
        .Builder()
        .client(build())
        .url(url.toHttpUrl())
        .bootstrapDnsHosts(
            ips.map { InetAddress.getByName(it) }
        )
        .build()
)



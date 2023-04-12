package com.kl3jvi.animity.utils

import android.util.Log
import com.kl3jvi.animity.data.enums.DnsTypes
import com.kl3jvi.animity.settings.Settings
import dev.brahmkshatriya.nicehttp.addGenericDns
import okhttp3.OkHttpClient

fun OkHttpClient.Builder.setGenericDns(settings: Settings) = apply {
    when (val dns = settings.selectedDns) {
        DnsTypes.GOOGLE_DNS -> addGenericDns(dns.url, dns.ipAddresses)
        DnsTypes.CLOUD_FLARE_DNS -> addGenericDns(dns.url, dns.ipAddresses)
        DnsTypes.AD_GUARD_DNS -> addGenericDns(dns.url, dns.ipAddresses)
    }
}

inline fun <reified T : Any, R : Any> T.printAny(
    tag: String? = null,
    message: String,
    printIf: () -> Boolean,
    transform: (T) -> R
): T {
    val logTag = tag ?: this.toString()
    Log.e(logTag, "[$this]->$message")
    if (printIf()) {
        val transformedList = (this as List<T>).map(transform)
        Log.e("PRINT IF", transformedList.toString())
    }
    return this
}

private fun <T : String?> T?.ifNull(function: () -> String): String {
    return this?.takeIf { it.isNotBlank() } ?: function()
}

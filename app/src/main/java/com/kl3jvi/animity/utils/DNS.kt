package com.kl3jvi.animity.utils

import android.util.Log
import com.kl3jvi.animity.settings.Settings
// import dev.brahmkshatriya.nicehttp.addGenericDns
import okhttp3.OkHttpClient

fun OkHttpClient.Builder.setGenericDns(settings: Settings) =
    apply {
        Log.e("Selected Dns", settings.selectedDns.name)
//    when (val dns = settings.selectedDns) {
//        DnsTypes.GOOGLE_DNS -> addGenericDns(dns.url, dns.ipAddresses)
//        DnsTypes.CLOUD_FLARE_DNS -> addGenericDns(dns.url, dns.ipAddresses)
//        DnsTypes.AD_GUARD_DNS -> addGenericDns(dns.url, dns.ipAddresses)
//    }
    }

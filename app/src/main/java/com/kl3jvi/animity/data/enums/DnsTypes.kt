package com.kl3jvi.animity.data.enums

enum class DnsTypes(val url: String, var ipAddresses: List<String>) {
    GOOGLE_DNS(
        url = "https://dns.google/dns-query",
        ipAddresses = listOf(
            "8.8.4.4",
            "8.8.8.8"
        )
    ),
    CLOUD_FLARE_DNS(
        url = "https://cloudflare-dns.com/dns-query",
        ipAddresses = listOf(
            "1.1.1.1",
            "1.0.0.1",
            "2606:4700:4700::1111",
            "2606:4700:4700::1001"
        )
    ),
    AD_GUARD_DNS(
        url = "https://dns.adguard.com/dns-query",
        ipAddresses = listOf(
            // "Non-filtering"
            "94.140.14.140",
            "94.140.14.141"
        )
    );

    companion object {
        private fun DnsTypes.toDisplayName(): String {
            return when (this) {
                GOOGLE_DNS -> "Google DNS"
                CLOUD_FLARE_DNS -> "CloudFlare DNS"
                AD_GUARD_DNS -> "AdGuard DNS"
            }
        }

        val dnsEntries = values().map { it.toDisplayName() }.toTypedArray()
    }
}

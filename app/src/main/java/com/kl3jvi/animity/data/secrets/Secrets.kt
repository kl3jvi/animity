package com.kl3jvi.animity.data.secrets

import com.kl3jvi.animity.BuildConfig

internal object Secrets {

    private external fun getAniListId(): ByteArray?
    private external fun getAniListSecret(): ByteArray?
    private external fun getRedirectUri(): ByteArray?
    private external fun getOneSignalKey(): ByteArray?


    val aniListId: String by lazy {
        decryptXOR(getAniListId() ?: ByteArray(0))
    }

    val aniListSecret: String by lazy {
        decryptXOR(getAniListSecret() ?: ByteArray(0))
    }

    val redirectUri: String by lazy {
        decryptXOR(getRedirectUri() ?: ByteArray(0))
    }

    val oneSignalKey: String by lazy {
        decryptXOR(getOneSignalKey() ?: ByteArray(0))
    }
}

private fun decryptXOR(data: ByteArray): String {
    val result = ByteArray(data.size)
    val keyBytes = BuildConfig.secretKey.toByteArray()

    for (i in data.indices) {
        result[i] = (data[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte()
    }

    return String(result, Charsets.UTF_8)
}

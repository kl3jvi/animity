package com.kl3jvi.animity.data.secrets

import com.kl3jvi.animity.BuildConfig

internal object Secrets {
    init {
        System.loadLibrary("keys")
    }

    private external fun getAniListId(): ByteArray?
    private external fun getAniListSecret(): ByteArray?
    private external fun getRedirectUri(): ByteArray?

    val aniListId = decryptXOR(getAniListId() ?: ByteArray(0))
    val aniListSecret = decryptXOR(getAniListSecret() ?: ByteArray(0))
    val redirectUri = decryptXOR(getRedirectUri() ?: ByteArray(0))
}

private fun decryptXOR(data: ByteArray): String {
    val result = ByteArray(data.size)
    val keyBytes = BuildConfig.secretKey.toByteArray()

    for (i in data.indices) {
        result[i] = (data[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte()
    }
    return result.toString(Charsets.UTF_8)
}


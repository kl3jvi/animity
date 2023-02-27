package com.kl3jvi.animity.data.secrets

internal object Secrets {

    init {
        System.loadLibrary("keys")
    }

    external fun getAnilistId(): String?
    external fun getAnilistSecret(): String?
    external fun getRedirectUri(): String?
}

package com.kl3jvi.animity.domain.repositories

import com.google.firebase.remoteconfig.FirebaseRemoteConfig

interface FirebaseRemoteConfigRepository {
    val instance: FirebaseRemoteConfig
    fun init()
    fun getBaseUrl(): String
}

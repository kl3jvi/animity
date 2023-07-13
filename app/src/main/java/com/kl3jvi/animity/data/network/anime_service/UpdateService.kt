package com.kl3jvi.animity.data.network.anime_service

import retrofit2.http.GET

interface UpdateService {

    @GET("https://raw.githubusercontent.com/kl3jvi/animity/main/update.json")
    suspend fun getUpdateInfo()
}

data class VersionInfo(
    val Animity: AnimityInfo
)

data class AnimityInfo(
    val x86: ApkInfo,
    val armeabi_v7a: ApkInfo,
    val arm64_v8a: ApkInfo,
    val x86_64: ApkInfo,
    val universal: ApkInfo
)

data class ApkInfo(
    val versionCode: Int,
    val versionName: String,
    val direct_link: String,
    val update_message: String,
    val apkSize: String
)



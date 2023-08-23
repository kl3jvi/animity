package com.kl3jvi.animity.data.network

import retrofit2.http.GET

interface UpdateService {
    @GET("https://raw.githubusercontent.com/kl3jvi/animity/main/app/versionInfo.json")
    suspend fun getUpdateInfo(): VersionInfo
}

data class VersionInfo(
    val Animity: AnimityInfo?,
)

data class AnimityInfo(
    val x86: ApkInfo,
    val armeabi_v7a: ApkInfo,
    val arm64_v8a: ApkInfo,
    val x86_64: ApkInfo,
    val universal: ApkInfo,
)

data class ApkInfo(
    val versionCode: Int,
    val versionName: String,
    val direct_link: String,
    val update_message: String,
    val apkSize: String,
)

fun getApkInfoForVersion(animityInfo: AnimityInfo, version: String): String {
    return when (version) {
        "x86" -> animityInfo.x86.direct_link
        "armeabi-v7a" -> animityInfo.armeabi_v7a.direct_link
        "arm64-v8a" -> animityInfo.arm64_v8a.direct_link
        "x86-64" -> animityInfo.x86_64.direct_link
        "universal" -> animityInfo.universal.direct_link
        else -> animityInfo.universal.direct_link
    }
}
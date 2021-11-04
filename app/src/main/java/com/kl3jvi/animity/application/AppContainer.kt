package com.kl3jvi.animity.application


import android.content.Context

import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.kl3jvi.animity.utils.Constants
import java.io.File


class AppContainer(
    val context: Context,
    exoDatabaseProvider: ExoDatabaseProvider
) {


    private fun getDataSourceFactory(): DefaultHttpDataSource.Factory {
        return DefaultHttpDataSource.Factory().apply {
            setUserAgent(Constants.USER_AGENT)
            val headers = mapOf(
                "referer" to Constants.REFERER,
                "accept" to "*/*",
                "sec-ch-ua" to "\"Chromium\";v=\"91\", \" Not;A Brand\";v=\"99\"",
                "sec-ch-ua-mobile" to "?0",
                "sec-fetch-user" to "?1",
                "sec-fetch-mode" to "navigate",
                "sec-fetch-dest" to "video"
            ) + Constants.getHeader() // Adds the headers from the provider, e.g Authorization
            setDefaultRequestProperties(headers)
        }
    }


    var downloadContentDirectory: File
    var downloadCache: Cache
    var downloadManager: DownloadManager

    init {
        downloadContentDirectory = File(context.getExternalFilesDir(null), "Animity")
        downloadCache =
            SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), exoDatabaseProvider)
        downloadManager = DownloadManager(
            context, exoDatabaseProvider, downloadCache, getDataSourceFactory(),
            Runnable::run
        )
    }

}
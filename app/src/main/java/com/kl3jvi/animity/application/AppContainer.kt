package com.kl3jvi.animity.application

import android.content.Context
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.kl3jvi.animity.utils.Constants.Companion.getDataSourceFactory
import java.io.File

class AppContainer(
    val context: Context,
) {
    private var dataBase: DatabaseProvider
    private var downloadContentDirectory: File
    var downloadCache: Cache
    var downloadManager: DownloadManager

    init {
        dataBase = StandaloneDatabaseProvider(context)
        downloadContentDirectory = File(context.getExternalFilesDir(null), "Animity")
        downloadCache = SimpleCache(downloadContentDirectory, NoOpCacheEvictor(), dataBase)
        downloadManager =
            DownloadManager(context, dataBase, downloadCache, getDataSourceFactory(), Runnable::run)
    }
}

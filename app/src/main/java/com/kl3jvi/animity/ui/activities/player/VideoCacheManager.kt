package com.kl3jvi.animity.ui.activities.player

import android.content.Context
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File

object VideoCacheManager {
    private var simpleCache: SimpleCache? = null

    /**
     * Returns the Singleton instance of [SimpleCache] that manages cached video files.
     * If the instance is not created, it creates it with the given context.
     *
     * @param context The application context.
     * @return The [SimpleCache] instance.
     */
    fun getInstance(context: Context): SimpleCache {
        val databaseProvider = StandaloneDatabaseProvider(context)
        if (simpleCache == null) {
            simpleCache = SimpleCache(
                File(
                    context.cacheDir,
                    "exoplayer"
                ).also { it.deleteOnExit() }, // Ensures always fresh file
                LeastRecentlyUsedCacheEvictor(300L * 1024L * 1024L),
                databaseProvider
            )
        }
        return simpleCache as SimpleCache
    }

    /**
     * Releases the Singleton instance of [SimpleCache] that manages cached video files.
     */
    fun release() {
        simpleCache?.release()
        simpleCache = null
    }
}

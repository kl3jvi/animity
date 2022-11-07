package com.kl3jvi.animity.data.network.anime_service

import com.kl3jvi.animity.data.model.ui_models.DetailedAnimeInfo
import com.kl3jvi.animity.data.model.ui_models.EpisodeWithTitle
import com.kl3jvi.animity.data.model.ui_models.GogoAnimeKeys
import com.kl3jvi.animity.utils.Constants.Companion.EPISODE_LOAD_URL
import com.kl3jvi.animity.utils.Constants.Companion.EPISODE_TITLES
import com.kl3jvi.animity.utils.Constants.Companion.GOGO_KEYS_URL
import com.kl3jvi.animity.utils.Constants.Companion.MAL_SYNC_URL
import okhttp3.ResponseBody
import retrofit2.http.*

/**w
 * Used to connect to the GogoAnime page to fetch animes
 */

interface GogoAnimeService {

    @GET
    suspend fun fetchEpisodeMediaUrl(
        @HeaderMap header: Map<String, String>,
        @Url url: String
    ): ResponseBody

    @GET
    suspend fun fetchAnimeInfo(
        @HeaderMap header: Map<String, String>,
        @Url episodeUrl: String
    ): ResponseBody

    @GET
    @Headers("watchsb:streamsb")
    suspend fun fetchM3u8Url(
        @HeaderMap header: Map<String, String>,
        @Url url: String
    ): ResponseBody

    @GET
    @Headers("X-Requested-With:XMLHttpRequest")
    suspend fun fetchM3u8PreProcessor(
        @HeaderMap header: Map<String, String>,
        @Url url: String
    ): ResponseBody

    @GET(EPISODE_LOAD_URL)
    suspend fun fetchEpisodeList(
        @HeaderMap header: Map<String, String>,
        @Query("ep_start") startEpisode: Int = 0,
        @Query("ep_end") endEpisode: String,
        @Query("id") id: String,
        @Query("default_ep") defaultEp: Int = 0,
        @Query("alias") alias: String
    ): ResponseBody

    @GET("$MAL_SYNC_URL/{id}.json")
    suspend fun getGogoUrlFromAniListId(
        @Path("id") id: Int
    ): DetailedAnimeInfo

    @GET("$EPISODE_TITLES/{id}.json")
    suspend fun getEpisodeTitles(@Path("id") id: Int = 1): EpisodeWithTitle

    @GET(GOGO_KEYS_URL)
    suspend fun getKeys(): GogoAnimeKeys
}

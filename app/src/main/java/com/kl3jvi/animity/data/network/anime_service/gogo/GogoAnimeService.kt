package com.kl3jvi.animity.data.network.anime_service.gogo

import com.kl3jvi.animity.data.model.ui_models.DetailedAnimeInfo
import com.kl3jvi.animity.data.network.anime_service.base.BaseService
import okhttp3.ResponseBody
import retrofit2.http.*

/**w
 * Used to connect to the GogoAnime page to fetch animes
 */

interface GogoAnimeService : BaseService {

    @GET
    suspend fun fetchEpisodeMediaUrl(
        @HeaderMap header: Map<String, String>,
        @Url url: String,
    ): ResponseBody

    @GET
    suspend fun fetchAnimeInfo(
        @HeaderMap header: Map<String, String>,
        @Url episodeUrl: String,
    ): ResponseBody

    @GET
    @Headers("watchsb:streamsb")
    suspend fun fetchM3u8Url(
        @HeaderMap header: Map<String, String>,
        @Url url: String,
    ): ResponseBody

    @GET
    @Headers("X-Requested-With:XMLHttpRequest")
    suspend fun fetchM3u8PreProcessor(
        @HeaderMap header: Map<String, String>,
        @Url url: String,
    ): ResponseBody

    @GET(EPISODE_LOAD_URL)
    suspend fun fetchEpisodeList(
        @HeaderMap header: Map<String, String>,
        @Query("ep_start") startEpisode: Int = 0,
        @Query("ep_end") endEpisode: String,
        @Query("id") id: String,
        @Query("default_ep") defaultEp: Int = 0,
        @Query("alias") alias: String,
    ): ResponseBody

    @GET("$MAL_SYNC_URL:{id}")
    suspend fun getGoGoDetailedUrl(
        @Path("id") id: Int,
    ): DetailedAnimeInfo

    companion object {
        const val MAL_SYNC_URL =
            "https://api.malsync.moe/mal/anime/anilist"

        const val EPISODE_LOAD_URL = "https://ajax.gogocdn.net/ajax/load-list-episode"

        /* Used to get the gogoanime keys from the GitHub repo. Thanks to https://github.com/justfoolingaround */
        const val GOGO_KEYS_URL =
            "https://raw.githubusercontent.com/justfoolingaround/animdl-provider-benchmarks/master/api/gogoanime.json"
    }
}

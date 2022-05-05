package com.kl3jvi.animity.data.network.anime_service

import com.kl3jvi.animity.data.model.ui_models.DetailedAnimeInfo
import com.kl3jvi.animity.utils.Constants.Companion.ANIME_SCHEDULE
import com.kl3jvi.animity.utils.Constants.Companion.EPISODE_LOAD_URL
import com.kl3jvi.animity.utils.Constants.Companion.MAL_SYNC_URL
import com.kl3jvi.animity.utils.Constants.Companion.SEARCH_URL
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Used to connect to the GogoAnime page to fetch animes
 */
interface AnimeService {

    @GET("https://ajax.gogocdn.net/ajax/page-recent-release.html")
    suspend fun fetchRecentSubOrDub(
        @HeaderMap header: Map<String, String>,
        @Query("page") page: Int,
        @Query("type") type: Int
    ): ResponseBody

    @GET("https://ajax.gogocdn.net/ajax/page-recent-release-ongoing.html")
    suspend fun fetchPopularFromAjax(
        @HeaderMap header: Map<String, String>,
        @Query("page") page: Int
    ): ResponseBody

    @GET("/anime-movies.html")
    suspend fun fetchMovies(
        @HeaderMap header: Map<String, String>,
        @Query("page") page: Int
    ): ResponseBody

    @GET("/new-season.html")
    suspend fun fetchNewestSeason(
        @HeaderMap header: Map<String, String>,
        @Query("page") page: Int
    ): ResponseBody

    @GET
    suspend fun fetchEpisodeMediaUrl(
        @HeaderMap header: Map<String, String>,
        @Url url: String
    ): ResponseBody

    @GET
    suspend fun fetchAnimeInfo(
        @HeaderMap header: Map<String, String>,
        @Url url: String
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

    @GET(SEARCH_URL)
    suspend fun fetchSearchData(
        @HeaderMap header: Map<String, String>,
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): ResponseBody

    @GET("$ANIME_SCHEDULE/{episodeUrl}")
    suspend fun fetchEpisodeTimeRelease(
        @Path("episodeUrl") episodeUrl: String
    ): ResponseBody

    @GET("$MAL_SYNC_URL/{id}.json")
    suspend fun getGogoUrlFromAniListId(
        @Path("id") id: Int = 1
    ): Response<DetailedAnimeInfo>

}


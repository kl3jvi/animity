package com.kl3jvi.animity.model.network

import com.kl3jvi.animity.utils.Constants
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {


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
    suspend fun FetchM3u8Url(
        @HeaderMap header: Map<String, String>,
        @Url url: String
    ): ResponseBody


    @GET(Constants.EPISODE_LOAD_URL)
    suspend fun fetchEpisodeList(
        @HeaderMap header: Map<String, String>,
        @Query("ep_start") startEpisode: Int = 0,
        @Query("ep_end") endEpisode: String,
        @Query("id") id: String,
        @Query("default_ep") defaultEp: Int = 0,
        @Query("alias") alias: String
    ): ResponseBody


    @GET(Constants.SEARCH_URL)
    suspend fun fetchSearchData(
        @HeaderMap header: Map<String, String>,
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): ResponseBody
}

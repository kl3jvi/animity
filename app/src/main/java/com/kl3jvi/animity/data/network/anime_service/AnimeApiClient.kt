package com.kl3jvi.animity.data.network.anime_service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.*
import javax.inject.Inject

class AnimeApiClient @Inject constructor(
    private val animeService: AnimeService,
    private val apolloClient: ApolloClient
) {
    suspend fun fetchRecentSubOrDub(
        header: Map<String, String>,
        page: Int,
        type: Int
    ) = animeService.fetchRecentSubOrDub(header, page, type)

    suspend fun fetchPopularFromAjax(
        header: Map<String, String>,
        page: Int
    ) = animeService.fetchPopularFromAjax(header, page)

    suspend fun fetchNewSeason(
        header: Map<String, String>,
        page: Int
    ) = animeService.fetchNewestSeason(header, page)

    suspend fun fetchMovies(
        header: Map<String, String>,
        page: Int
    ) = animeService.fetchMovies(header, page)

    suspend fun fetchAnimeInfo(
        header: Map<String, String>,
        episodeUrl: String
    ) = animeService.fetchAnimeInfo(header, episodeUrl)

    suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ) = animeService.fetchEpisodeList(
        header = header,
        id = id,
        endEpisode = endEpisode,
        alias = alias
    )

    suspend fun fetchEpisodeTimeRelease(episodeUrl: String) =
        animeService.fetchEpisodeTimeRelease(episodeUrl)

    suspend fun fetchEpisodeMediaUrl(header: Map<String, String>, url: String) =
        animeService.fetchEpisodeMediaUrl(header, url)

    suspend fun fetchM3u8Url(header: Map<String, String>, url: String) =
        animeService.fetchM3u8Url(header, url)

    suspend fun getKeys() = animeService.getKeys()

    suspend fun fetchSearchData(header: Map<String, String>, keyword: String, page: Int) =
        animeService.fetchSearchData(header, keyword, page)

    suspend fun fetchM3u8PreProcessor(header: Map<String, String>, url: String) =
        animeService.fetchM3u8PreProcessor(header, url)

    suspend fun getGogoUrlFromAniListId(id: Int) = animeService.getGogoUrlFromAniListId(id)

    /**
     * `getHomeData()` is a function that returns a Flow of HomeDataQuery.HomeDataQuery.Data
     */
    fun getHomeData() = apolloClient.query(HomeDataQuery()).toFlow()

    /**
     * "Get the profile data for the user with the given ID, or the current user if no ID is given."
     *
     * The first thing to notice is that the function is marked as suspend. This means that it can be
     * called from a coroutine
     *
     * @param userId The user ID of the user whose profile data we want to fetch.
     */
    fun getProfileData(userId: Int?) =
        apolloClient.query(UserQuery(Optional.presentIfNotNull(userId))).toFlow()

    /**
     * "Get the anime list data for the given user ID, if it's not null."
     *
     * The first thing to notice is that the function is marked as suspend. This means that it can be
     * called from a coroutine
     *
     * @param userId The user's ID.
     */
    fun getAnimeListData(userId: Int?) =
        apolloClient.query(AnimeListCollectionQuery(Optional.presentIfNotNull(userId))).toFlow()

    /**
     * It takes a query and a page number, and returns a Flow of SearchAnimeQuery.Data
     *
     * @param query The search query
     * @param page Int - The page number to fetch.
     */
    fun fetchSearchAniListData(query: String, page: Int) =
        apolloClient.query(
            SearchAnimeQuery(
                Optional.presentIfNotNull(query),
                Optional.presentIfNotNull(page),
            )
        ).toFlow()

    /**
     * `getFavoriteAnimesFromAniList` is a function that takes in a userId and a page number and
     * returns a Flow of FavoriteAnimeQuery.Data
     *
     * @param userId The user's id from AniList.
     * @param page The page number of the results to be returned.
     */
    fun getFavoriteAnimesFromAniList(
        userId: Int?,
        page: Int?
    ) = apolloClient.query(
        FavoritesAnimeQuery(
            Optional.Present(userId),
            Optional.Present(page)
        )
    ).toFlow()
}

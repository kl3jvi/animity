package com.kl3jvi.animity.data.network.anime_service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.*
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class AnimeApiClient @Inject constructor(
    private val animeService: AnimeService,
    private val apolloClient: ApolloClient
) {

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

    suspend fun fetchEpisodeMediaUrl(
        header: Map<String, String>,
        url: String
    ) = animeService.fetchEpisodeMediaUrl(header, url)

    suspend fun fetchM3u8Url(
        header: Map<String, String>,
        url: String
    ) = animeService.fetchM3u8Url(header, url)

    suspend fun getKeys() = animeService.getKeys()

    suspend fun fetchM3u8PreProcessor(
        header: Map<String, String>,
        url: String
    ) = animeService.fetchM3u8PreProcessor(header, url)

    suspend fun getGogoUrlFromAniListId(id: Int) =
        animeService.getGogoUrlFromAniListId(id)

    suspend fun getEpisodeTitles(id: Int) =
        animeService.getEpisodeTitles(id)

    fun getHomeData() = apolloClient.query(
        HomeDataQuery()
    ).toFlow().catch { e -> logError(e) }

    fun getProfileData(userId: Int?) =
        apolloClient.query(
            UserQuery(Optional.presentIfNotNull(userId))
        ).toFlow().catch { e -> logError(e) }

    fun getAnimeListData(userId: Int?) =
        apolloClient.query(
            AnimeListCollectionQuery(Optional.presentIfNotNull(userId))
        ).toFlow().catch { e -> logError(e) }

    fun fetchSearchAniListData(query: String, page: Int) =
        apolloClient.query(
            SearchAnimeQuery(
                Optional.presentIfNotNull(query),
                Optional.presentIfNotNull(page),
            )
        ).toFlow().catch { e -> logError(e) }


    fun getFavoriteAnimesFromAniList(
        userId: Int?,
        page: Int?
    ) = apolloClient.query(
        FavoritesAnimeQuery(
            Optional.Present(userId),
            Optional.Present(page)
        )
    ).toFlow().catch { e -> logError(e) }
}

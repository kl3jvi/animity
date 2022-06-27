package com.kl3jvi.animity.data.network.anime_service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.*
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class GogoAnimeApiClient @Inject constructor(
    private val gogoAnimeService: GogoAnimeService,
    private val apolloClient: ApolloClient
) {
    /**
     * It fetches the anime info from the given episode url
     *
     * @param header Map<String, String>
     * @param episodeUrl The url of the episode you want to fetch the info from.
     */
    suspend fun fetchAnimeInfo(
        header: Map<String, String>,
        episodeUrl: String
    ) = gogoAnimeService.fetchAnimeInfo(header, episodeUrl)

    suspend fun fetchEpisodeList(
        header: Map<String, String>,
        id: String,
        endEpisode: String,
        alias: String
    ) = gogoAnimeService.fetchEpisodeList(
        header = header,
        id = id,
        endEpisode = endEpisode,
        alias = alias
    )

    /**
     * It fetches the media url of an episode
     *
     * @param header Map<String, String>
     * @param url The url of the episode you want to fetch the media url from.
     */
    suspend fun fetchEpisodeMediaUrl(
        header: Map<String, String>,
        episodeUrl: String
    ) = gogoAnimeService.fetchEpisodeMediaUrl(header, episodeUrl)

    suspend fun fetchM3u8Url(
        header: Map<String, String>,
        url: String
    ) = gogoAnimeService.fetchM3u8Url(header, url)

    suspend fun getEncryptionKeys() = gogoAnimeService.getKeys()

    suspend fun fetchM3u8PreProcessor(
        header: Map<String, String>,
        url: String
    ) = gogoAnimeService.fetchM3u8PreProcessor(header, url)

    suspend fun getGogoUrlFromAniListId(id: Int) =
        gogoAnimeService.getGogoUrlFromAniListId(id)

    suspend fun getEpisodeTitles(id: Int) =
        gogoAnimeService.getEpisodeTitles(id)

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

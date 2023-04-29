package com.kl3jvi.animity.data.network.anilist_service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.AnimeListEntryMutation
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.NotificationsQuery
import com.kl3jvi.animity.SaveMediaMutation
import com.kl3jvi.animity.SearchAnimeQuery
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.ToggleFavouriteMutation
import com.kl3jvi.animity.TrendingMediaQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.type.MediaListStatus
import javax.inject.Inject

class AniListGraphQlClient @Inject constructor(
    private val apolloClient: ApolloClient
) : AniListSync {

    override suspend fun getHomeData() =
        apolloClient.query(
            HomeDataQuery()
        ).execute()

    override suspend fun getProfileData(userId: Int?) =
        apolloClient.query(
            UserQuery(Optional.presentIfNotNull(userId))
        ).execute()

    override suspend fun getAnimeListData(userId: Int?) =
        apolloClient.query(
            AnimeListCollectionQuery(Optional.presentIfNotNull(userId))
        ).execute()

    override suspend fun fetchSearchAniListData(query: String, page: Int) =
        apolloClient.query(
            SearchAnimeQuery(
                Optional.presentIfNotNull(query),
                Optional.presentIfNotNull(page)
            )
        ).execute()

    override suspend fun getFavoriteAnimesFromAniList(
        userId: Int?,
        page: Int?
    ) = apolloClient.query(
        FavoritesAnimeQuery(
            Optional.Present(userId),
            Optional.Present(page)
        )
    ).execute()

    override suspend fun getSessionForUser() = apolloClient.query(SessionQuery()).execute()

    override suspend fun getUserData(id: Int?) =
        apolloClient.query(UserQuery(Optional.Present(id))).execute()

    override suspend fun getFavoriteAnimes(
        userId: Int?,
        page: Int?
    ) = apolloClient.query(
        FavoritesAnimeQuery(
            Optional.Present(userId),
            Optional.Present(page)
        )
    ).execute()

    override suspend fun getTopTenTrending() = apolloClient.query(TrendingMediaQuery()).execute()

    override suspend fun markAnimeAsFavorite(animeId: Int?) = apolloClient.mutation(
        ToggleFavouriteMutation(
            Optional.Present(animeId)
        )
    ).execute()

    override suspend fun getNotifications(page: Int) =
        apolloClient.query(
            NotificationsQuery(
                Optional.Present(page)
            )
        ).execute()

    override suspend fun markAnimeStatus(mediaId: Int, status: MediaListStatus) =
        apolloClient.mutation(SaveMediaMutation(mediaId, status)).execute()
}

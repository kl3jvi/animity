package com.kl3jvi.animity.data.network.anilist_service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.SearchAnimeQuery
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.ToggleFavouriteMutation
import com.kl3jvi.animity.TrendingMediaQuery
import com.kl3jvi.animity.UserQuery
import javax.inject.Inject

class AniListGraphQlClient @Inject constructor(
    private val apolloClient: ApolloClient
) : AniListSync {

    override fun getHomeData() =
        apolloClient.query(
            HomeDataQuery()
        ).toFlow()

    override fun getProfileData(userId: Int?) =
        apolloClient.query(
            UserQuery(Optional.presentIfNotNull(userId))
        ).toFlow()

    override fun getAnimeListData(userId: Int?) =
        apolloClient.query(
            AnimeListCollectionQuery(Optional.presentIfNotNull(userId))
        ).toFlow()

    override fun fetchSearchAniListData(query: String, page: Int) =
        apolloClient.query(
            SearchAnimeQuery(
                Optional.presentIfNotNull(query),
                Optional.presentIfNotNull(page)
            )
        ).toFlow()

    override fun getFavoriteAnimesFromAniList(
        userId: Int?,
        page: Int?
    ) = apolloClient.query(
        FavoritesAnimeQuery(
            Optional.Present(userId),
            Optional.Present(page)
        )
    ).toFlow()

    override fun getSessionForUser() = apolloClient.query(SessionQuery()).toFlow()

    override fun getUserData(id: Int?) =
        apolloClient.query(UserQuery(Optional.Present(id))).toFlow()

    override fun getFavoriteAnimes(
        userId: Int?,
        page: Int?
    ) = apolloClient.query(
        FavoritesAnimeQuery(
            Optional.Present(userId),
            Optional.Present(page)
        )
    ).toFlow()

    override fun getTopTenTrending() = apolloClient.query(TrendingMediaQuery()).toFlow()

    override fun markAnimeAsFavorite(animeId: Int?) = apolloClient.mutation(
        ToggleFavouriteMutation(
            Optional.Present(animeId)
        )
    ).toFlow()
}

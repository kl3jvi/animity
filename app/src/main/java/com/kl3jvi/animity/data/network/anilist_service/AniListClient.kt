package com.kl3jvi.animity.data.network.anilist_service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.*
import com.kl3jvi.animity.data.model.auth_models.AniListAuth
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AniListClient @Inject constructor(
    private val aniListService: AniListService,
    private val apolloClient: ApolloClient,
    private val ioDispatcher: CoroutineDispatcher
) : AniListSync {



    override fun getHomeData() = apolloClient.query(
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
                Optional.presentIfNotNull(page),
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

    override fun getSessionForUser(): Flow<ApolloResponse<SessionQuery.Data>> {
        return try {
            apolloClient.query(SessionQuery()).toFlow().catch { e -> logError(e) }
        } catch (e: Exception) {
            logError(e)
            emptyFlow()
        }
    }

    override fun getUserData(id: Int?): Flow<ApolloResponse<UserQuery.Data>> {
        return try {
            apolloClient.query(UserQuery(Optional.Present(id))).toFlow().catch { e -> logError(e) }
        } catch (e: Exception) {
            logError(e)
            emptyFlow()
        }
    }


    override fun getFavoriteAnimes(
        userId: Int?,
        page: Int?
    ): Flow<ApolloResponse<FavoritesAnimeQuery.Data>> {
        return try {
            apolloClient.query(
                FavoritesAnimeQuery(
                    Optional.Present(userId),
                    Optional.Present(page)
                )
            ).toFlow().catch { e -> logError(e) }
        } catch (e: Exception) {
            logError(e)
            emptyFlow()
        }
    }

    override fun getTopTenTrending(): Flow<ApolloResponse<TrendingMediaQuery.Data>> {
        return try {
            apolloClient.query(TrendingMediaQuery()).toFlow().catch { e -> logError(e) }
        } catch (e: Exception) {
            logError(e)
            emptyFlow()
        }
    }

    override fun markAnimeAsFavorite(animeId: Int?): Flow<ApolloResponse<ToggleFavouriteMutation.Data>> {
        return try {
            apolloClient.mutation(
                ToggleFavouriteMutation(
                    Optional.Present(animeId)
                )
            ).toFlow().catch { e -> logError(e) }
                .flowOn(ioDispatcher)
        } catch (e: Exception) {
            logError(e)
            emptyFlow()
        }
    }

}
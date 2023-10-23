package com.kl3jvi.animity.data.network.anilist_service

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.AiringQuery
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.GetFollowersListQuery
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.NotificationsQuery
import com.kl3jvi.animity.SaveMediaListEntryMutation
import com.kl3jvi.animity.SaveMediaMutation
import com.kl3jvi.animity.SearchAnimeQuery
import com.kl3jvi.animity.SearchUsersQuery
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.ToggleFavouriteMutation
import com.kl3jvi.animity.ToggleFollowUserMutation
import com.kl3jvi.animity.TrendingMediaQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.analytics.Performance
import com.kl3jvi.animity.type.MediaListStatus
import com.kl3jvi.animity.type.MediaSort
import javax.inject.Inject

class AniListGraphQlClient
    @Inject
    constructor(
        private val apolloClient: ApolloClient,
        private val performance: Performance,
    ) : AniListSync {
        // 1. Queries related to users:
        override suspend fun getHomeData() =
            performance.measureAndTrace("homeData") {
                apolloClient.query(HomeDataQuery()).execute()
            }

        override suspend fun getSessionForUser() =
            performance.measureAndTrace("sessionUserData") {
                apolloClient.query(SessionQuery()).execute()
            }

        override suspend fun getUserDataById(userId: Int?) =
            performance.measureAndTrace("userDataById") {
                apolloClient.query(UserQuery(Optional.presentIfNotNull(userId))).execute()
            }

        override suspend fun fetchUsers(
            query: String,
            page: Int,
        ) = performance.measureAndTrace("fetchUsers") {
            apolloClient.query(
                SearchUsersQuery(
                    Optional.presentIfNotNull(query),
                    Optional.presentIfNotNull(page),
                ),
            ).execute()
        }

        override suspend fun getUserData(id: Int?) =
            performance.measureAndTrace("getUserData") {
                apolloClient.query(UserQuery(Optional.Present(id))).execute()
            }

        override suspend fun getNotifications(page: Int) =
            performance.measureAndTrace("getNotifications") {
                apolloClient.query(NotificationsQuery(Optional.Present(page))).execute()
            }

        override suspend fun getFollowersAndFollowing(page: Int) =
            performance.measureAndTrace("getFollowersAndFollowing") {
                apolloClient.query(GetFollowersListQuery(page)).execute()
            }

        // 2. Queries related to anime media:

        override suspend fun getAnimeListData(userId: Int?) =
            performance.measureAndTrace("getAnimeListData") {
                apolloClient.query(AnimeListCollectionQuery(Optional.presentIfNotNull(userId)))
                    .execute()
            }

        override suspend fun fetchSearchAniListData(
            query: String,
            page: Int,
            toMediaSort: List<MediaSort>,
        ) = performance.measureAndTrace("fetchSearchAniListData") {
            apolloClient.query(
                SearchAnimeQuery(
                    Optional.presentIfNotNull(query),
                    Optional.presentIfNotNull(page),
                    Optional.present(toMediaSort),
                ),
            ).execute()
        }

        override suspend fun getFavoriteAnimes(
            userId: Int?,
            page: Int?,
        ) = performance.measureAndTrace("getFavoriteAnimes") {
            apolloClient.query(
                FavoritesAnimeQuery(
                    Optional.Present(userId),
                    Optional.Present(page),
                ),
            ).execute()
        }

        override suspend fun getTopTenTrending() =
            performance.measureAndTrace("getTopTenTrending") {
                apolloClient.query(TrendingMediaQuery()).execute()
            }

        override suspend fun getAiringAnimeForDate(
            startDate: Int?,
            endDate: Int?,
        ) = performance.measureAndTrace("getAiringAnimeForDate") {
            apolloClient.query(AiringQuery(Optional.present(startDate), Optional.present(endDate)))
                .execute()
        }

        // 3. Mutation related operations:

        override suspend fun markAnimeAsFavorite(animeId: Int?) =
            performance.measureAndTrace("markAnimeAsFavorite") {
                apolloClient.mutation(ToggleFavouriteMutation(Optional.Present(animeId))).execute()
            }

        override suspend fun markAnimeStatus(
            mediaId: Int,
            status: MediaListStatus,
        ) = performance.measureAndTrace("markAnimeStatus") {
            apolloClient.mutation(SaveMediaMutation(mediaId, status)).execute()
        }

        override suspend fun markWatchedEpisode(
            mediaId: Int,
            episodesWatched: Int,
        ) = performance.measureAndTrace("markWatchedEpisode") {
            apolloClient.mutation(SaveMediaListEntryMutation(mediaId, episodesWatched)).execute()
        }

        override suspend fun followUser(id: Int) =
            performance.measureAndTrace("followUser") {
                apolloClient.mutation(ToggleFollowUserMutation(Optional.present(id))).execute()
            }
    }

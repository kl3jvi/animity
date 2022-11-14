package com.kl3jvi.animity.data.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.ProfileData
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.domain.repositories.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher
) : ProfileRepository {

    /**
     * We get the profile data from the AniList API, then we get the profile row from the AniList API,
     * then we combine the two into a single object
     *
     * @param userId The user id of the user you want to get the profile data of.
     */
    override fun getProfileData(userId: Int?) =
        aniListGraphQlClient.getProfileData(userId)
            .mapNotNull(ApolloResponse<UserQuery.Data>::convert)
            .combine(getProfileAnimes(userId)) { userData, profileRow ->
                ProfileData(
                    userData = userData,
                    profileRow = profileRow
                )
            }.flowOn(ioDispatcher)

    /**
     * We're using the `AniListGraphQlClient` to get the anime list data for a user, and then we're
     * converting the response to a `AnimeListCollection` object.
     *
     * @param userId The user's ID.
     */
    private fun getProfileAnimes(userId: Int?) =
        aniListGraphQlClient.getAnimeListData(userId)
            .mapNotNull(ApolloResponse<AnimeListCollectionQuery.Data>::convert)
}

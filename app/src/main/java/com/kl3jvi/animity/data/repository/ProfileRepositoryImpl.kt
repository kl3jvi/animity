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
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher
) : ProfileRepository {

    override fun getProfileData(userId: Int?) = flow {
        emit(aniListGraphQlClient.getProfileData(userId))
    }.mapNotNull(ApolloResponse<UserQuery.Data>::convert)
        .combine(getProfileAnimes(userId)) { userData, profileRow ->
            ProfileData(
                userData = userData,
                profileRow = profileRow
            )
        }.flowOn(ioDispatcher)

    private fun getProfileAnimes(userId: Int?) = flow {
        emit(aniListGraphQlClient.getAnimeListData(userId))
    }.mapNotNull(ApolloResponse<AnimeListCollectionQuery.Data>::convert)
        .flowOn(ioDispatcher)
}

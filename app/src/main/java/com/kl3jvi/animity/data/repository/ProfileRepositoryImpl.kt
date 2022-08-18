package com.kl3jvi.animity.data.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.domain.repositories.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher
) : ProfileRepository {

    override fun getProfileData(userId: Int?) =
        aniListGraphQlClient.getProfileData(userId)
            .mapNotNull(ApolloResponse<UserQuery.Data>::convert)
            .flowOn(ioDispatcher)


    override fun getProfileAnimes(userId: Int?) =
        aniListGraphQlClient.getAnimeListData(userId)
            .mapNotNull(ApolloResponse<AnimeListCollectionQuery.Data>::convert)
            .flowOn(ioDispatcher)

}












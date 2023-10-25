package com.kl3jvi.animity.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.model.ui_models.ProfileData
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.data.paging.FollowersPagingSource
import com.kl3jvi.animity.domain.repositories.ProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull

class ProfileRepositoryImpl
@Inject
constructor(
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher,
) : ProfileRepository {
    override fun getProfileData(userId: Int?) =
        flow {
            emit(aniListGraphQlClient.getUserDataById(userId))
        }.mapNotNull(ApolloResponse<UserQuery.Data>::convert).run {
            combine(
                this,
                getProfileAnimes(userId),
                getNumberOfFollowingAndFollowers(),
            ) { userData, profileRow, followStuff ->
                ProfileData(
                    userData = userData,
                    profileRow = profileRow,
                    followersAndFollowing = followStuff,
                )
            }
        }.flowOn(ioDispatcher)

    private fun getProfileAnimes(userId: Int?) =
        flow {
            emit(aniListGraphQlClient.getAnimeListData(userId).convert())
        }.flowOn(ioDispatcher)

    private fun getNumberOfFollowingAndFollowers(): Flow<PagingData<Pair<List<User>, List<User>>>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = true, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { FollowersPagingSource(aniListGraphQlClient) },
        ).flow.flowOn(ioDispatcher)
    }

    override fun followUser(userId: Int) =
        flow {
            val followState = aniListGraphQlClient.followUser(userId).convert()
            emit(followState)
        }.flowOn(ioDispatcher)

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}

package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.MediaIdFromNameQuery
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAnimeDetailsFromAnilistUseCase @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(query: String?): Flow<ApolloResponse<MediaIdFromNameQuery.Data>> {
        return userRepositoryImpl.getMediaId(query = query).flowOn(ioDispatcher)
    }
}
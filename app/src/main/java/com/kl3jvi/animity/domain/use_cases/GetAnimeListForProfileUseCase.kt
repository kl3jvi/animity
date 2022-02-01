package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAnimeListForProfileUseCase @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(userId: Int?): Flow<ApolloResponse<AnimeListCollectionQuery.Data>> {
        return userRepositoryImpl.getAnimeListData(userId = userId).flowOn(ioDispatcher)
    }
}
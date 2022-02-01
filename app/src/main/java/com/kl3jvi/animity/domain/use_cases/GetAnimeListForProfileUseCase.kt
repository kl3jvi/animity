package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAnimeListForProfileUseCase @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl
) {
    operator fun invoke(userId: Int?): Flow<ApolloResponse<AnimeListCollectionQuery.Data>> {
        return userRepositoryImpl.getAnimeListData(userId = userId)
    }
}
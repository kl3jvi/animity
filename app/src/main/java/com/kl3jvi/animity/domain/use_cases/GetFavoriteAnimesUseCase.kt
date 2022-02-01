package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFavoriteAnimesUseCase @Inject constructor(
    private val userRepository: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(userId: Int?, page: Int?): Flow<ApolloResponse<FavoritesAnimeQuery.Data>> {
        return userRepository.getFavoriteAnimes(userId, page).flowOn(ioDispatcher)
    }
}